import React from "react";
import { Button } from "@mui/material";

/**
 * TopBar component renders analysis type tabs: FIRST, FOLLOW, PREDICT, and LL1.
 * Disables tabs if grammar input is empty or an error exists.
 * Highlights the currently active analysis type.
 *
 * @param {string} currentAnalysisType - Currently selected analysis type.
 * @param {string} grammar - Raw grammar input text.
 * @param {function} handleAnalysisTypeChange - Callback to switch analysis type.
 * @param {string|null} error - Error message; if present, disables switching.
 */
const TopBar = React.memo(({ currentAnalysisType, grammar, handleAnalysisTypeChange, error }) => {
    // Determine if grammar input is empty or there is an error
    const isGrammarEmpty = grammar.trim() === "";
    const hasError = Boolean(error);

    return (
        <div className="top-bar">
            {/* Render a tab button for each analysis type */}
            {["FIRST", "FOLLOW", "PREDICT", "LL1"].map((type) => (
                <Button
                    key={type}
                    onClick={() => handleAnalysisTypeChange(type)}
                    className={`tab-btn ${currentAnalysisType === type ? "active" : ""}`}
                    disabled={isGrammarEmpty || hasError} // Disable if no grammar or error
                >
                    {type}
                </Button>
            ))}
        </div>
    );
});

export default TopBar;
