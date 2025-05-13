import React, { useCallback, useMemo, useEffect, useRef, useState } from "react";
import { Typography } from "@mui/material";
import throttle from "lodash.throttle";
import { mapBackendErrorToTranslation } from "../../utils/errorMapper";

import "../../assets/styles/grammar/GrammarAnalysis.css";

import { ReactComponent as SunIcon } from "../../assets/icons/sun.svg";
import { ReactComponent as MoonIcon } from "../../assets/icons/moon.svg";

import TopBar from "./TopBar";
import MobileBlocker from "../blocker/MobileBlocker";
import GrammarInput from "./GrammarInput";
import PseudoCodeBlock from "./PseudoCodeBlock";
import StepDetails from "./StepDetails";
import ResultDisplay from "./ResultDisplay";
import LL1TableDisplay from "./LL1TableDisplay";
import Controls from "./Controls";
import Divider from "./Divider";
import HelpDialog from "../help/HelpDialog";
import { pseudoCodeMapping } from "../../utils/PseudoCodeMapping";
import { useTranslation } from "react-i18next";
import LanguageSwitcher from "../switcher/LanguageSwitcher";
import api from "../../services/axiosInstance";

/**
 * Main component for grammar analysis.
 * Manages theme, input, analysis state, and layout resizing.
 */
const GrammarAnalysis = () => {
    const [theme, setTheme] = useState("light");
    const [currentAnalysisType, setCurrentAnalysisType] = useState("FIRST");
    const [currentStepIndex, setCurrentStepIndex] = useState(0);
    const [pseudoCodeLine, setPseudoCodeLine] = useState(0);
    const [isLoading, setIsLoading] = useState(false);
    const [grammar, setGrammar] = useState("");
    const [error, setError] = useState(null);
    const [dividerPos, setDividerPos] = useState(50);
    const draggingRef = useRef(false);
    const [cachedResults, setCachedResults] = useState({});
    const [totalSteps, setTotalSteps] = useState(0);
    const isFirstRenderRef = useRef(true);
    const [lastStepTimestamp, setLastStepTimestamp] = useState(0);
    const { t } = useTranslation();

    // Holds dynamic result data and details for display
    const [analysisData, setAnalysisData] = useState({
        dynamicResult: {},
        stepDetails: "",
        ll1Table: {},
        ll1: false,
    });

    // State for showing transformed grammar when EBNF input
    const [showTransformedInput, setShowTransformedInput] = useState(false);
    const [transformedGrammar, setTransformedGrammar] = useState("");

    /**
     * Toggle light/dark theme and update body class.
     */
    const toggleTheme = () => {
        const newTheme = document.body.classList.contains("dark") ? "light" : "dark";
        document.body.classList.toggle("dark");
        setTheme(newTheme);
    };

    /**
     * Fetches a specific step from the backend or cache.
     * @param stepIndex index of the step to retrieve
     */
    const fetchStep = useCallback(
        async (stepIndex) => {
            const trimmedGrammar = grammar.trim();
            const cacheKey = `${currentAnalysisType}-${trimmedGrammar}-${stepIndex}`;
            // Use cache if available
            if (cachedResults[cacheKey]) {
                const cached = cachedResults[cacheKey];
                setAnalysisData(cached.analysisData);
                setPseudoCodeLine(cached.pseudoCodeLine);
                setCurrentStepIndex(stepIndex);
                setTotalSteps(cached.totalSteps);
                return;
            }

            setIsLoading(true);
            try {
                const { data } = await api.post(
                    "/grammar/step",
                    {
                        analysisType: currentAnalysisType,
                        stepIndex,
                        grammar: trimmedGrammar,
                    }
                );

                const computedAnalysisData = {
                    dynamicResult: data.partialResult || {},
                    stepDetails:
                        Object.values(data.currentStepDetails || {}).flat().join("\n") || "",
                    ll1Table: data.ll1Table || {},
                    ll1: data.ll1 ?? false,
                    productionRuleList: data.productionRuleList || [],
                    productionRuleNumbers: data.productionRuleNumbers || {},
                };

                const computedPseudoCodeLine = Math.max(1, data.pseudoCodeLine || 1) - 1;
                setAnalysisData(computedAnalysisData);
                setPseudoCodeLine(computedPseudoCodeLine);
                setCurrentStepIndex(data.currentStepIndex);
                setTotalSteps(data.totalSteps);

                setCachedResults((prev) => ({
                    ...prev,
                    [cacheKey]: {
                        analysisData: computedAnalysisData,
                        pseudoCodeLine: computedPseudoCodeLine,
                        totalSteps: data.totalSteps,
                    },
                }));
            } catch (err) {
                console.error("Error loading step:", err);
                const serverErrors = err.response?.data?.errors;
                if (Array.isArray(serverErrors) && serverErrors.length > 0) {
                    const translated = serverErrors.map((msg) => mapBackendErrorToTranslation(msg, t));
                    setError(translated.join("; "));
                } else {
                    setError(t("analysisError"));
                }
            } finally {
                setIsLoading(false);
            }
        },
        [currentAnalysisType, grammar, cachedResults]
    );

    /**
     * Performs full grammar analysis on submit and fetches initial step.
     */
    const fetchAnalysis = useCallback(async () => {
        if (!grammar.trim()) {
            setError(t("Grammar cannot be empty!"));
            return;
        }
        setIsLoading(true);
        setError(null);
        setCurrentStepIndex(0);

        try {
            const { data } = await api.post(
                "/grammar/analyze",
                { grammar: grammar.trim() }
            );

            await fetchStep(0);

            if (data.transformedGrammar && data.transformedGrammar !== grammar.trim()) {
                setTransformedGrammar(data.transformedGrammar);
            } else {
                setTransformedGrammar("");
                setShowTransformedInput(false);
            }
        } catch (err) {
            console.error("Grammar Analysis Error:", err);
            const serverErrors = err.response?.data?.errors;
            if (Array.isArray(serverErrors) && serverErrors.length > 0) {
                const translated = serverErrors.map((msg) => mapBackendErrorToTranslation(msg, t));
                setError(translated.join("; "));
            } else {
                setError(t("analysisError"));
            }
        } finally {
            setIsLoading(false);
        }

    }, [grammar, fetchStep, t]);

    /**
     * Effect: on analysis type change, fetch or load from cache.
     */
    useEffect(() => {
        setPseudoCodeLine(0);

        if (!grammar.trim()) {
            setCachedResults({});
            setAnalysisData({
                dynamicResult: {},
                stepDetails: "",
                ll1Table: {},
                ll1: false,
            });
            setCurrentStepIndex(0);
            setPseudoCodeLine(0);
            return;
        }

        if (isFirstRenderRef.current) {
            isFirstRenderRef.current = false;
            fetchAnalysis();
            return;
        }

        const cacheKey = currentAnalysisType;
        if (cachedResults[cacheKey]) {
            const cached = cachedResults[cacheKey];
            setAnalysisData({
                dynamicResult: cached.dynamicResult,
                stepDetails: cached.stepDetails,
                ll1Table: cached.ll1Table,
                ll1: cached.ll1 ?? false,
                productionRuleList: cached.productionRuleList || [],
                productionRuleNumbers: cached.productionRuleNumbers || {},
            });
            setCurrentStepIndex(cached.stepIndex);
            setPseudoCodeLine(cached.pseudoLine);
            setTotalSteps(cached.totalSteps);
        } else {
            fetchAnalysis();
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [currentAnalysisType]);

    /**
     * Effect: re-run analysis when grammar input changes.
     */
    useEffect(() => {
        setCachedResults({});
        if (grammar.trim()) {
            fetchAnalysis();
        } else {
            setAnalysisData({
                dynamicResult: {},
                stepDetails: "",
                ll1Table: {},
                ll1: false,
            });
            setCurrentStepIndex(0);
            setPseudoCodeLine(0);
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [grammar]);

    /**
     * Handler to navigate between steps with rate limiting.
     */
    const handleStep = async (stepIndex) => {
        const now = Date.now();
        if (now - lastStepTimestamp < 300 || isLoading) return;

        setLastStepTimestamp(now);

        if (stepIndex === 0 && !grammar.trim()) {
            setError(null);
            setAnalysisData({
                dynamicResult: {},
                stepDetails: "",
                ll1Table: {},
                ll1: false,
            });
            setCurrentStepIndex(0);
            setPseudoCodeLine(0);
            return;
        }

        const targetIndex = stepIndex === "RESULT" ? 9999 : stepIndex;
        await fetchStep(targetIndex);
    };

    /**
     * Cache current analysis and switch type.
     */
    const handleAnalysisTypeChange = async (type) => {
        setCachedResults((prev) => ({
            ...prev,
            [currentAnalysisType]: {
                dynamicResult: analysisData.dynamicResult,
                stepDetails: analysisData.stepDetails,
                ll1Table: analysisData.ll1Table,
                ll1: analysisData.ll1,
                productionRuleList: analysisData.productionRuleList,
                productionRuleNumbers: analysisData.productionRuleNumbers,
                stepIndex: currentStepIndex,
                pseudoLine: pseudoCodeLine,
                totalSteps,
            },
        }));

        setCurrentAnalysisType(type);
    };

    // Divider drag handlers
    const handleMouseDown = () => {
        draggingRef.current = true;
    };

    const throttledMouseMove = useMemo(
        () =>
            throttle((e) => {
                if (!draggingRef.current) return;
                const newPos = Math.max(
                    25,
                    Math.min(75, (e.clientX / window.innerWidth) * 100)
                );
                setDividerPos(newPos);
            }, 16),
        [setDividerPos]
    );

    const handleMouseMove = useCallback((e) => {
        throttledMouseMove(e);
    }, [throttledMouseMove]);

    const handleMouseUp = useCallback(() => {
        draggingRef.current = false;
    }, []);

    useEffect(() => {
        document.addEventListener("mousemove", handleMouseMove);
        document.addEventListener("mouseup", handleMouseUp);
        return () => {
            document.removeEventListener("mousemove", handleMouseMove);
            document.removeEventListener("mouseup", handleMouseUp);
        };
    }, [handleMouseMove, handleMouseUp]);

    // Auto-scroll highlighted pseudocode line
    useEffect(() => {
        const element = document.querySelector(".pseudo-code .highlighted");
        if (element) {
            element.scrollIntoView({ behavior: "smooth", block: "center" });
        }
    }, [pseudoCodeLine]);

    // Determine pseudocode lines for current type
    const pseudoCodeLines = pseudoCodeMapping[currentAnalysisType] || [];

    return (
        <>
            <MobileBlocker />
            <div className={`analysis-container ${theme}`}>
                <div className="top-bar-wrapper">
                    <TopBar
                        currentAnalysisType={currentAnalysisType}
                        grammar={grammar}
                        handleAnalysisTypeChange={handleAnalysisTypeChange}
                        error={error}
                    />
                    <div style={{ display: "flex", alignItems: "center", gap: "10px" }}>
                        <div
                            className={`theme-toggle ${theme === "dark" ? "active" : ""}`}
                            onClick={toggleTheme}
                            title={t("Toggle theme")}
                        >
                            {theme === "light" ? <MoonIcon /> : <SunIcon />}
                        </div>
                        <HelpDialog />
                        <LanguageSwitcher />
                    </div>
                </div>

                <div className="main-container">
                    <div className="left-panel" style={{ width: `${dividerPos}%` }}>
                        <GrammarInput
                            grammar={showTransformedInput ? transformedGrammar : grammar}
                            setGrammar={showTransformedInput ? () => {} : setGrammar}
                            error={error}
                            readOnly={showTransformedInput}
                            showTransformedInput={showTransformedInput}
                            setShowTransformedInput={setShowTransformedInput}
                            transformedGrammar={transformedGrammar}
                        />

                        {currentAnalysisType !== "LL1" && (
                            <>
                                <Typography variant="h6">{currentAnalysisType} Algorithm</Typography>
                                <PseudoCodeBlock
                                    key={currentAnalysisType}
                                    pseudoCodeLines={pseudoCodeLines}
                                    pseudoCodeLine={pseudoCodeLine}
                                />
                                <StepDetails stepDetails={analysisData.stepDetails} />
                            </>
                        )}
                    </div>

                    <Divider handleMouseDown={handleMouseDown} />

                    <div className="right-panel" style={{ width: `${100 - dividerPos}%` }}>
                        {currentAnalysisType !== "LL1" ? (
                            <ResultDisplay
                                dynamicResult={analysisData.dynamicResult}
                                analysisType={currentAnalysisType}
                            />
                        ) : (
                            <LL1TableDisplay
                                ll1Table={analysisData.ll1Table}
                                productionRuleList={analysisData.productionRuleList}
                                productionRuleNumbers={analysisData.productionRuleNumbers}
                                ll1={analysisData.ll1}
                            />
                        )}

                        {currentAnalysisType !== "LL1" && (
                            <Controls
                                currentStepIndex={currentStepIndex}
                                isLoading={isLoading}
                                grammar={grammar}
                                handleStep={handleStep}
                                totalSteps={totalSteps}
                                error={error}
                            />
                        )}
                    </div>
                </div>
            </div>
        </>
    );
};

export default GrammarAnalysis;
