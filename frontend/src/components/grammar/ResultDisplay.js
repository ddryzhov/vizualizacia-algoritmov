import React, { useMemo } from "react";
import { Typography, Paper } from "@mui/material";
import {useTranslation} from "react-i18next";

/**
 * Displays the current dynamic result (FIRST/FOLLOW/PREDICT set).
 * Filters out intermediate non-terminals generated from EBNF.
 */
const isIntermediateNonTerminal = (nonTerminal) => /^_.+$/.test(nonTerminal);

const ResultDisplay = React.memo(({ dynamicResult, analysisType }) => {
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
            <Typography variant="h6">{t("Result")}</Typography>
            <Paper className="result-box">
                {hasData ? (
                    Object.entries(grouped).map(([lhs, valueSet]) => {
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
                    <Typography>{t("No data available")}</Typography>
                )}
            </Paper>
        </>
    );
});

export default ResultDisplay;
