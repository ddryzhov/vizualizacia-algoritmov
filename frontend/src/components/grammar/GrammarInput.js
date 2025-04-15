import React, { useMemo, useState, useEffect } from "react";
import { Typography, Alert } from "@mui/material";
import debounce from "lodash.debounce";

const GrammarInput = React.memo(
    ({
         grammar,
         setGrammar,
         error,
         readOnly = false,
         showTransformedInput = false,
         setShowTransformedInput = () => {},
         transformedGrammar = "",
     }) => {
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
                <div className="grammar-header-row">
                    <Typography variant="h6" style={{ margin: 0 }}>
                        Enter Grammar
                    </Typography>
                    <button
                        className="toggle-btn"
                        onClick={() => setShowTransformedInput((prev) => !prev)}
                        disabled={!grammar.trim() || !transformedGrammar.trim()}
                        title="Toggle between original EBNF and transformed BNF"
                    >
                        {showTransformedInput ? "Show Original EBNF" : "Show Transformed BNF"}
                    </button>
                </div>

                {error && <Alert severity="error">{error}</Alert>}

                <textarea
                    className="grammar-input"
                    value={localGrammar}
                    onChange={readOnly ? undefined : handleGrammarChange}
                    placeholder={`Example:\nS \\to a [b] [c \\mid d] {e f}`}
                    rows={10}
                    readOnly={readOnly}
                />
            </>
        );
    }
);

export default GrammarInput;
