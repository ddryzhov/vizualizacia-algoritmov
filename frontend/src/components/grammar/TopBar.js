import React from "react";
import { Button } from "@mui/material";

/**
 * Displays analysis type tabs (FIRST, FOLLOW, PREDICT, LL1).
 * Highlights the active tab and disables switching if grammar is empty.
 */
const TopBar = React.memo(({ currentAnalysisType, grammar, handleAnalysisTypeChange, error }) => {
    const isGrammarEmpty = grammar.trim() === "";
    const hasError = Boolean(error);

    return (
        <div className="top-bar">
            {["FIRST", "FOLLOW", "PREDICT", "LL1"].map((type) => (
                <Button
                    key={type}
                    onClick={() => handleAnalysisTypeChange(type)}
                    className={`tab-btn ${currentAnalysisType === type ? "active" : ""}`}
                    disabled={isGrammarEmpty || hasError}
                >
                    {type}
                </Button>
            ))}
        </div>
    );
});

export default TopBar;
