import React, { useMemo, useState, useEffect } from "react";
import { Typography, Alert, Button } from "@mui/material";
import debounce from "lodash.debounce";
import { useTranslation } from "react-i18next";

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
        const { t } = useTranslation();

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

        const handleFileUpload = (e) => {
            const file = e.target.files?.[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = (event) => {
                    const text = event.target.result;
                    if (typeof text === "string") {
                        setGrammar(text);
                    }
                };
                reader.readAsText(file);
            }

            e.target.value = null;
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
                        {t("Enter Grammar")}
                    </Typography>
                    <div style={{ display: "flex", gap: "10px", alignItems: "center" }}>
                        <button
                            className="toggle-btn"
                            onClick={() => setShowTransformedInput((prev) => !prev)}
                            disabled={!grammar.trim() || !transformedGrammar.trim()}
                            title={t("Toggle between original EBNF and transformed BNF")}
                        >
                            {showTransformedInput
                                ? t("Show Original EBNF")
                                : t("Show Transformed BNF")}
                        </button>
                        {!readOnly && (
                            <>
                                <input
                                    accept=".txt"
                                    id="upload-grammar"
                                    type="file"
                                    style={{ display: "none" }}
                                    onChange={handleFileUpload}
                                />
                                <label htmlFor="upload-grammar">
                                    <Button variant="outlined" component="span" size="small">
                                        {t("Upload .txt")}
                                    </Button>
                                </label>
                            </>
                        )}
                    </div>
                </div>

                {error && <Alert severity="error">{error}</Alert>}

                <textarea
                    className="grammar-input"
                    value={localGrammar}
                    onChange={readOnly ? undefined : handleGrammarChange}
                    placeholder={`Example:\nS -> 'a' A | 'b' B\nA -> 'c' | epsilon\nB -> 'd'`}
                    rows={10}
                    readOnly={readOnly}
                />
            </>
        );
    }
);

export default GrammarInput;
