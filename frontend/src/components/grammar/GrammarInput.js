import React, { useMemo, useState, useEffect } from "react";
import { Typography, Alert } from "@mui/material";
import debounce from "lodash.debounce";

/**
 * Input field for grammar with TeX-to-symbol substitutions.
 * Supports debounced updates and error display.
 */
const GrammarInput = React.memo(({ grammar, setGrammar, error }) => {
    const [localGrammar, setLocalGrammar] = useState(grammar);

    const debouncedSetGrammar = useMemo(
        () => debounce((val) => setGrammar(val), 500),
        [setGrammar]
    );

    const handleGrammarChange = (e) => {
        const updatedGrammar = e.target.value
            .replace(/\\eps/g, "epsilon")
            .replace(/\\to/g, "->")
            .replace(/\\mid/g, "|");
        setLocalGrammar(updatedGrammar);
        debouncedSetGrammar(updatedGrammar);
    };

    useEffect(() => {
        setLocalGrammar(grammar);
    }, [grammar]);

    useEffect(() => {
        return () => {
            debouncedSetGrammar.cancel();
        };
    }, [debouncedSetGrammar]);

    return (
        <>
            <Typography variant="h6" gutterBottom>
                Enter Grammar
            </Typography>
            {error && <Alert severity="error">{error}</Alert>}
            <textarea
                className="grammar-input"
                value={localGrammar}
                onChange={handleGrammarChange}
                placeholder={`Example:\nS \\to a [b] [c \\mid d] {e f}`}
                rows={10}
            />
        </>
    );
});

export default GrammarInput;
