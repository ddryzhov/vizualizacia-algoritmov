import React from "react";
import { Button } from "@mui/material";

/**
 * Renders step control buttons: PREV, NEXT, RESET, RESULT.
 * Enables or disables buttons based on step index and loading state.
 */
const Controls = React.memo(({
                                 currentStepIndex,
                                 isLoading,
                                 grammar,
                                 handleStep,
                                 totalSteps,
                             }) => {
    const isGrammarEmpty = grammar.trim() === "";
    const disabledPrev = currentStepIndex <= 0 || isLoading || isGrammarEmpty;
    const disabledNext = currentStepIndex >= totalSteps - 1 || isLoading || isGrammarEmpty;
    const disabledReset = isLoading || isGrammarEmpty;
    const disabledResult = currentStepIndex >= totalSteps - 1 || isLoading || isGrammarEmpty;

    return (
        <div className="controls">
            <Button onClick={() => handleStep(currentStepIndex - 1)} disabled={disabledPrev}>
                PREV
            </Button>
            <Button onClick={() => handleStep(currentStepIndex + 1)} disabled={disabledNext}>
                NEXT
            </Button>
            <Button onClick={() => handleStep(0)} disabled={disabledReset}>
                RESET
            </Button>
            <Button onClick={() => handleStep(totalSteps - 1)} disabled={disabledResult}>
                RESULT
            </Button>
        </div>
    );
});

export default Controls;
