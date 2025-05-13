import React from "react";
import {
    Typography,
    Paper,
    Table,
    TableHead,
    TableBody,
    TableRow,
    TableCell,
    Box,
    Alert
} from "@mui/material";
import { useTranslation } from "react-i18next";
import "../../assets/styles/grammar/GrammarAnalysis.css";

/**
 * Component to render the LL(1) parsing table with conflict highlighting
 * and an optional list of grammar rules with their numeric labels.
 *
 * @param {Object} props
 * @param {Object<string, Object<string, string>>} props.ll1Table - Nested map: non-terminal -> terminal -> rule labels
 * @param {string[]} props.productionRuleList - Array of full production rule strings
 * @param {Object<string, number>} props.productionRuleNumbers - Map from rule string to its numeric index
 * @param {boolean} props.ll1 - Flag indicating if grammar is LL(1) (no conflicts)
 */
const LL1TableDisplay = React.memo(({ ll1Table, productionRuleList, productionRuleNumbers, ll1 }) => {
    const { t } = useTranslation();

    // Determine non-terminals from table keys; do not render if empty
    const nonTerminals = Object.keys(ll1Table || {});
    if (nonTerminals.length === 0) return null;

    // Determine terminals from the first non-terminal's entry, excluding end marker
    const terminals = Object.keys(ll1Table[nonTerminals[0]] || {}).filter(t => t !== "$");

    /**
     * Converts full rule string to its short label (e.g., "R3").
     */
    const getShortLabel = (rule) => {
        if (!rule || !productionRuleNumbers) return rule;
        const ruleNumber = productionRuleNumbers[rule];
        return ruleNumber ? `R${ruleNumber}` : rule;
    };

    return (
        <div className="ll1-table">
            {/* Table title */}
            <Typography variant="h5" gutterBottom>{t("parseTableTitle")}</Typography>

            {/* LL(1) status alert */}
            <Alert severity={ll1 ? "success" : "error"} className="ll1-alert">
                {ll1 ? t("ll1Success") : t("ll1Error")}
            </Alert>

            <Paper>
                <Table className="ll1-inner-table">
                    <TableHead>
                        <TableRow>
                            {/* Header for non-terminal column */}
                            <TableCell className="ll1-header">{t("nonTerminal")}</TableCell>
                            {/* Headers for each terminal */}
                            {terminals.map((term) => (
                                <TableCell key={term} className="ll1-header">{term}</TableCell>
                            ))}
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {/* Render a row for each non-terminal */}
                        {nonTerminals.map((nt) => (
                            <TableRow key={nt}>
                                {/* Non-terminal label cell */}
                                <TableCell className="ll1-nonterminal">{nt}</TableCell>
                                {/* Cells for each terminal */}
                                {terminals.map((term) => {
                                    const rule = ll1Table[nt]?.[term] || "";
                                    // Split multiple rules, map to short labels
                                    const label = rule
                                        .split(", ")
                                        .map((r) => getShortLabel(r))
                                        .join(", ");
                                    const isConflict = rule.includes(",");
                                    return (
                                        <TableCell
                                            key={term}
                                            className={isConflict ? "conflict-cell" : "ll1-cell"}
                                        >
                                            {label || "-"}
                                        </TableCell>
                                    );
                                })}
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </Paper>

            {/* List of full grammar rules */}
            {productionRuleList?.length > 0 && (
                <Box sx={{ mt: 3 }}>
                    <Typography variant="subtitle1" className="ll1-rules-title">
                        {t("grammarRules")}
                    </Typography>
                    <ul className="ll1-rule-list">
                        {productionRuleList.map((rule, idx) => (
                            <li key={idx}>R{idx + 1}: {rule}</li>
                        ))}
                    </ul>
                </Box>
            )}
        </div>
    );
});

export default LL1TableDisplay;
