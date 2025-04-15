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
import "../../assets/styles/grammar/GrammarAnalysis.css";

/**
 * Displays the LL(1) parsing table and rule list.
 * Highlights table conflicts and shows rule numbers.
 */
const LL1TableDisplay = React.memo(({ ll1Table, productionRuleList, productionRuleNumbers, ll1 }) => {
    const nonTerminals = Object.keys(ll1Table || {});
    if (nonTerminals.length === 0) return null;

    const terminals = Object.keys(ll1Table[nonTerminals[0]] || {}).filter(t => t !== "$");

    const getShortLabel = (rule) => {
        if (!rule || !productionRuleNumbers) return rule;
        const ruleNumber = productionRuleNumbers[rule];
        return ruleNumber ? `R${ruleNumber}` : rule;
    };

    return (
        <div className="ll1-table">
            <Typography variant="h5" gutterBottom>Parse Table</Typography>

            <Alert severity={ll1 ? "success" : "error"} className="ll1-alert">
                {ll1
                    ? "✅ Grammar is LL(1): No conflicts in the table."
                    : "❌ Grammar is NOT LL(1): Conflicts found in the table."}
            </Alert>

            <Paper>
                <Table className="ll1-inner-table">
                    <TableHead>
                        <TableRow>
                            <TableCell className="ll1-header">Non-terminal</TableCell>
                            {terminals.map((term) => (
                                <TableCell key={term} className="ll1-header">{term}</TableCell>
                            ))}
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {nonTerminals.map((nt) => (
                            <TableRow key={nt}>
                                <TableCell className="ll1-nonterminal">{nt}</TableCell>
                                {terminals.map((term) => {
                                    const rule = ll1Table[nt]?.[term] || "";
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

            {productionRuleList?.length > 0 && (
                <Box sx={{ mt: 3 }}>
                    <Typography variant="subtitle1" className="ll1-rules-title">
                        Grammar Rules:
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
