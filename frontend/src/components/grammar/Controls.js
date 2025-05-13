import React from "react";
import { Button } from "@mui/material";

/**
 * Step control buttons component.
 * Renders PREV, NEXT, RESET, and RESULT buttons.
 * Buttons are enabled/disabled based on current step, loading state, grammar input, and errors.
 *
 * @param {number} currentStepIndex - Index of the current analysis step.
 * @param {boolean} isLoading - True if analysis is in progress.
 * @param {string} grammar - The raw grammar input text.
 * @param {function} handleStep - Callback to change to a specific step index.
 * @param {number} totalSteps - Total number of available steps.
 * @param {string|null} error - Error message, if any.
 */
const Controls = React.memo(({
                                 currentStepIndex,
                                 isLoading,
                                 grammar,
                                 handleStep,
                                 totalSteps,
                                 error,
                             }) => {
    // Check if grammar input is empty or there is an error
    const isGrammarEmpty = grammar.trim() === "";
    const hasError = Boolean(error);

    // Determine disabled state for each button
    const disabledPrev = currentStepIndex <= 0 || isLoading || isGrammarEmpty || hasError;
    const disabledNext = currentStepIndex >= totalSteps - 1 || isLoading || isGrammarEmpty || hasError;
    const disabledReset = isLoading || isGrammarEmpty || hasError;
    const disabledResult = currentStepIndex >= totalSteps - 1 || isLoading || isGrammarEmpty || hasError;

    return (
        <div className="controls">
            {/* Navigate to previous step */}
            <Button onClick={() => handleStep(currentStepIndex - 1)} disabled={disabledPrev}>
                PREV
            </Button>
            {/* Navigate to next step */}
            <Button onClick={() => handleStep(currentStepIndex + 1)} disabled={disabledNext}>
                NEXT
            </Button>
            {/* Reset to first step */}
            <Button onClick={() => handleStep(0)} disabled={disabledReset}>
                RESET
            </Button>
            {/* Jump to final result step */}
            <Button onClick={() => handleStep(totalSteps - 1)} disabled={disabledResult}>
                RESULT
            </Button>
        </div>
    );
});

export default Controls;
