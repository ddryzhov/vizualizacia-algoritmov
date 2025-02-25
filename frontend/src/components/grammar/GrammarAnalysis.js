import React, {useCallback, useEffect, useRef, useState} from "react";
import {Alert, Box, Button, Paper, Table, TableBody, TableCell, TableHead, TableRow, Typography} from "@mui/material";
import "../../assets/styles/grammar/GrammarAnalysis.css";
import {pseudoCodeMapping} from "./PseudoCodeMapping";
import axios from "axios";
import { MathJax, MathJaxContext } from "better-react-mathjax";

const GrammarAnalysis = () => {
    const [currentAnalysisType, setCurrentAnalysisType] = useState("FIRST");
    const [currentStepIndex, setCurrentStepIndex] = useState(0);
    const [pseudoCodeLine, setPseudoCodeLine] = useState(0);
    const [isLoading, setIsLoading] = useState(false);
    const [grammar, setGrammar] = useState("");
    const [error, setError] = useState(null);
    const [dividerPos, setDividerPos] = useState(50);
    const draggingRef = useRef(false);
    const [cachedResults, setCachedResults] = useState({});

    const [analysisData, setAnalysisData] = useState({
        dynamicResult: {},
        stepDetails: "",
        ll1Table: {},
        isLL1: false,
        ll1Description: "",
    });

    const fetchStep = useCallback(async (stepIndex) => {
        setIsLoading(true);
        try {
            const { data } = await axios.get("http://localhost:8080/api/grammar/step", {
                params: {
                    analysisType: currentAnalysisType,
                    stepIndex,
                    grammar: grammar.trim(),
                }
            });

            console.log("Server response:", data);

            setAnalysisData((prev) => ({
                ...prev,
                dynamicResult: data.partialResult || {},
                stepDetails: Object.values(data.currentStepDetails || {}).flat().join("\n"),
                ll1Table: data.ll1Table || {},
                isLL1: !!data.isLL1,
                ll1Description: data.ll1Description || "",
            }));

            setPseudoCodeLine(Math.max(1, data.pseudoCodeLine || 1) - 1);
        } catch (error) {
            console.error("Error loading a step:", error);
        } finally {
            setIsLoading(false);
        }
    }, [currentAnalysisType, grammar]);

    const fetchAnalysis = useCallback(async () => {
        if (!grammar.trim()) {
            setError("Grammar cannot be empty!");
            return;
        }

        setIsLoading(true);
        setError(null);
        setCurrentStepIndex(0);

        try {
            console.log("Sending a request to the server:", grammar);
            await axios.post("http://localhost:8080/api/grammar/analyze", { grammar: grammar.trim() });
            await fetchStep(0);
        } catch (error) {
            console.error("Grammar Analysis Error:", error);
            setError("Analysis error. Check your grammar or try again.");
        } finally {
            setIsLoading(false);
        }
    }, [grammar, fetchStep]);

    useEffect(() => {
        if (!cachedResults[currentAnalysisType] && grammar.trim()) {
            fetchAnalysis();
        }
    }, [grammar, currentAnalysisType, fetchAnalysis, cachedResults]);

    useEffect(() => {
        setCachedResults({});
        setAnalysisData({
            dynamicResult: {},
            stepDetails: "",
            ll1Table: {},
            isLL1: false,
            ll1Description: "",
        });
        setCurrentStepIndex(0);
        setPseudoCodeLine(0);
    }, [grammar, setCachedResults, setAnalysisData, setCurrentStepIndex, setPseudoCodeLine]);

    const mathJaxRef = useRef(null);
    const [isRendered, setIsRendered] = useState(false);

    useEffect(() => {
        setIsRendered(false);

        const renderMathJax = async () => {
            if (window.MathJax && mathJaxRef.current) {
                await window.MathJax.typesetPromise();
                setIsRendered(true);
            }
        };

        setTimeout(renderMathJax, 20);
    }, [currentAnalysisType, pseudoCodeLine]);


    useEffect(() => {
        setCachedResults({});
        setAnalysisData({
            dynamicResult: {},
            stepDetails: "",
            ll1Table: {},
            isLL1: false,
            ll1Description: "",
        });
        setCurrentStepIndex(0);
        setPseudoCodeLine(0);
    }, [grammar]);

    const handleStep = async (stepIndex) => {
        await fetchStep(stepIndex);
        setCurrentStepIndex(stepIndex);
    };

    const handleAnalysisTypeChange = async (type) => {
        setCachedResults(prev => ({
            ...prev,
            [currentAnalysisType]: {
                dynamicResult: analysisData.dynamicResult,
                stepDetails: analysisData.stepDetails,
                ll1Table: analysisData.ll1Table,
                isLL1: analysisData.isLL1,
                ll1Description: analysisData.ll1Description,
                stepIndex: currentStepIndex,
                pseudoLine: pseudoCodeLine,
            }
        }));

        setCurrentAnalysisType(type);

        if (cachedResults[type]) {
            const { dynamicResult, stepDetails, ll1Table, isLL1, ll1Description, stepIndex, pseudoLine } = cachedResults[type];

            setAnalysisData({
                dynamicResult: dynamicResult || {},
                stepDetails: stepDetails || "",
                ll1Table: ll1Table || {},
                isLL1: isLL1 || false,
                ll1Description: ll1Description || "",
            });

            setCurrentStepIndex(stepIndex || 0);
            setPseudoCodeLine(pseudoLine || 0);
        } else {
            if (grammar.trim()) {
                await fetchAnalysis();
            }
        }
    };

    const handleMouseDown = () => {
        draggingRef.current = true;
    };

    const handleMouseMove = useCallback((e) => {
        if (!draggingRef.current) return;

        requestAnimationFrame(() => {
            const newPos = Math.max(25, Math.min(75, (e.clientX / window.innerWidth) * 100));
            setDividerPos(newPos);
        });
    }, []);

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

    return (
        <div className="analysis-container">
            <Typography variant="h4" className="title">Grammar Analyzer</Typography>

            <div className="top-bar">
                {["FIRST", "FOLLOW", "PREDICT", "LL1"].map((type) => (
                    <Button
                        key={type}
                        onClick={() => handleAnalysisTypeChange(type)}
                        className={`tab-btn ${currentAnalysisType === type ? "active" : ""}`}
                    >
                        {type}
                    </Button>
                ))}
            </div>

            <div className="main-container">
                <div className="left-panel" style={{width: `${dividerPos}%`}}>
                    <Typography variant="h6">Enter Grammar</Typography>
                    {error && <Alert severity="error">{error}</Alert>}
                    <textarea
                        className="grammar-input"
                        value={grammar}
                        onChange={(e) => setGrammar(e.target.value)}
                        placeholder="S -> A | B"
                    />

                    {currentAnalysisType !== "LL1" && (
                        <>
                            <Typography variant="h6">{currentAnalysisType} Algorithm</Typography>
                            <MathJaxContext>
                                <Paper className="pseudo-code" style={{ opacity: isRendered ? 1 : 0, transition: "opacity 0.2s ease-in-out" }}>
                                    {pseudoCodeMapping[currentAnalysisType].map((line, index) => (
                                        <Box ref={mathJaxRef} key={currentAnalysisType + "-" + index} className={index === pseudoCodeLine ? "highlighted" : ""}>
                                            <MathJax>{`\\( ${line} \\)`}</MathJax>
                                        </Box>
                                    ))}
                                </Paper>
                            </MathJaxContext>

                            <Typography variant="h6" className="step-details-title">Step Details</Typography>
                            <Paper className="details-box">
                                <Typography style={{ whiteSpace: "pre-line" }}>
                                    {analysisData.stepDetails || "No details available"}
                                </Typography>
                            </Paper>
                        </>
                    )}
                </div>

                <div className="divider" onMouseDown={handleMouseDown}/>

                <div className="right-panel" style={{width: `${100 - dividerPos}%` }}>
                    {currentAnalysisType !== "LL1" && (
                        <>
                    <Typography variant="h6">Result</Typography>
                    <Paper className="result-box">
                        {analysisData.dynamicResult && Object.keys(analysisData.dynamicResult).length > 0 ? (
                            Object.entries(analysisData.dynamicResult).map(([key, values]) => (
                                <div key={key}>
                                    <strong>{key}</strong>: {Array.isArray(values) ? values.join(", ") : values}
                                </div>
                            ))
                        ) : (
                            <Typography>No data available</Typography>
                        )}
                    </Paper>
                        </>
                    )}

                    {currentAnalysisType === "LL1" && Object.keys(analysisData.ll1Table || {}).length > 0 && (
                        <div className="ll1-table">
                            <Typography variant="h5">LL(1) Table</Typography>
                            <Paper>
                                <Table>
                                    <TableHead>
                                        <TableRow>
                                            <TableCell>Non-terminal</TableCell>
                                            {Object.keys(analysisData.ll1Table[Object.keys(analysisData.ll1Table)[0]] || {}).map(term => (
                                                <TableCell key={term}>{term}</TableCell>
                                            ))}
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {Object.keys(analysisData.ll1Table).map(nt => (
                                            <TableRow key={nt}>
                                                <TableCell>{nt}</TableCell>
                                                {Object.entries(analysisData.ll1Table[nt] || {}).map(([term, rule]) => {
                                                    const hasConflict = rule.includes(",");
                                                    return (
                                                        <TableCell
                                                            key={term}
                                                            className={hasConflict ? "conflict-cell" : ""}
                                                        >
                                                            {rule || "-"}
                                                        </TableCell>
                                                    );
                                                })}
                                            </TableRow>
                                        ))}
                                    </TableBody>
                                </Table>
                            </Paper>
                        </div>
                    )}

                    {currentAnalysisType !== "LL1" && (
                        <div className="controls">
                            <Button onClick={() => handleStep(currentStepIndex - 1)} disabled={currentStepIndex === 0 || isLoading}>
                                PREV
                            </Button>
                            <Button
                                onClick={() => handleStep(currentStepIndex + 1)}
                                disabled={isLoading || pseudoCodeLine + 1 >= pseudoCodeMapping[currentAnalysisType].length}
                            >
                                NEXT
                            </Button>
                            <Button onClick={() => handleStep(0)} disabled={isLoading}>
                                RESET
                            </Button>
                            <Button onClick={() => handleStep(9999)} disabled={isLoading}>
                                RESULT
                            </Button>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default GrammarAnalysis;
