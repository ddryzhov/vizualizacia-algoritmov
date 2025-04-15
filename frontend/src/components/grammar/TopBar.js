import React from "react";
import { Button } from "@mui/material";

/**
 * Displays analysis type tabs (FIRST, FOLLOW, PREDICT, LL1).
 * Highlights the active tab and disables switching if grammar is empty.
 */
const TopBar = React.memo(({ currentAnalysisType, grammar, handleAnalysisTypeChange }) => {
    const isGrammarEmpty = grammar.trim() === "";
    return (
        <div className="top-bar">
            {["FIRST", "FOLLOW", "PREDICT", "LL1"].map((type) => (
                <Button
                    key={type}
                    onClick={() => handleAnalysisTypeChange(type)}
                    className={`tab-btn ${currentAnalysisType === type ? "active" : ""}`}
                    disabled={isGrammarEmpty}
                >
                    {type}
                </Button>
            ))}
        </div>
    );
});

export default TopBar;
