import React, { useMemo } from "react";
import { Typography, Paper } from "@mui/material";
import {useTranslation} from "react-i18next";

/**
 * Component to display dynamic analysis results (FIRST, FOLLOW, or PREDICT sets).
 * Groups values by non-terminal and filters out helper non-terminals generated during EBNF transformation.
 *
 * @param {Object<string, string[]>} dynamicResult - Map from production key ("A -> α") to array of set values.
 * @param {string} analysisType - The type of analysis (e.g., "FIRST", "FOLLOW", "PREDICT").
 */
const isIntermediateNonTerminal = (nonTerminal) => /^_.+$/.test(nonTerminal);

const ResultDisplay = React.memo(({ dynamicResult, analysisType }) => {
    /**
     * Memoized grouping of dynamicResult by LHS non-terminal.
     * Converts array of values into a Set for uniqueness.
     */
    const grouped = useMemo(() => {
        const res = {};
        if (dynamicResult && Object.keys(dynamicResult).length > 0) {
            Object.entries(dynamicResult).forEach(([rule, values]) => {
                const lhs = rule.split("->")[0].trim();
                if (!res[lhs]) res[lhs] = new Set();
                values.forEach((v) => res[lhs].add(v));
            });
        }
        return res;
    }, [dynamicResult]);

    const { t } = useTranslation();
    const hasData = Object.keys(grouped).length > 0;

    return (
        <>
            {/* Section title */}
            <Typography variant="h6">{t("Result")}</Typography>
            <Paper className="result-box">
                {hasData ? (
                    Object.entries(grouped).map(([lhs, valueSet]) => {
                        // Label like "FIRST(A)"
                        const label = `${analysisType}(${lhs})`;

                        return (
                            <div
                                key={lhs}
                                className={isIntermediateNonTerminal(lhs) ? "ebnf-nonterminal" : ""}
                                style={{ padding: "4px 6px", borderBottom: "1px solid #ddd" }}
                            >
                                <strong>{label}</strong> = &#123; {[...valueSet].join(", ")} &#125;
                            </div>
                        );
                    })
                ) : (
                    // Fallback when no data
                    <Typography>{t("No data available")}</Typography>
                )}
            </Paper>
        </>
    );
});

export default ResultDisplay;
