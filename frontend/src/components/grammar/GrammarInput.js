import React, { useMemo, useState, useEffect } from "react";
import { Typography, Alert, Button } from "@mui/material";
import debounce from "lodash.debounce";
import { useTranslation } from "react-i18next";

/**
 * GrammarInput component for entering or uploading grammar definitions in BNF/EBNF.
 * Supports debounced input, file upload, and toggling between original and transformed grammar.
 *
 * @param {string} grammar - Current grammar text.
 * @param {function} setGrammar - Setter to update grammar in parent state.
 * @param {string|null} error - Error message to display, if any.
 * @param {boolean} readOnly - If true, disables editing and file upload.
 * @param {boolean} showTransformedInput - Flag to show transformed grammar textarea.
 * @param {function} setShowTransformedInput - Toggles between original and transformed grammar.
 * @param {string} transformedGrammar - Transformed grammar text to display when toggled.
 */
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
        // Local state mirrors `grammar` for immediate textarea updates
        const [localGrammar, setLocalGrammar] = useState(grammar);
        const { t } = useTranslation();

        // Debounced setter avoids rapid state updates on every keystroke
        const debouncedSetGrammar = useMemo(
            () => debounce((val) => setGrammar(val), 500),
            [setGrammar]
        );

        /**
         * Handles textarea changes: normalizes escape sequences and updates both local and parent state.
         */
        const handleGrammarChange = (e) => {
            const updatedGrammar = e.target.value
                .replace(/\\eps/g, "epsilon")
                .replace(/\\to/g, "->")
                .replace(/\\mid/g, "|");
            setLocalGrammar(updatedGrammar);
            debouncedSetGrammar(updatedGrammar);
        };

        /**
         * Handles .txt file upload: reads file as text and updates parent grammar.
         */
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

        // Sync local state when parent `grammar` prop changes
        useEffect(() => {
            setLocalGrammar(grammar);
        }, [grammar]);

        // Cleanup debounce on unmount
        useEffect(() => {
            return () => {
                debouncedSetGrammar.cancel();
            };
        }, [debouncedSetGrammar]);

        return (
            <>
                {/* Header row: title, toggle, and upload controls */}
                <div className="grammar-header-row">
                    <Typography variant="h6" style={{ margin: 0 }}>
                        {t("Enter Grammar")}
                    </Typography>
                    <div style={{ display: "flex", gap: "10px", alignItems: "center" }}>
                        {/* Toggle between original and transformed grammar view */}
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
                        {/* File upload button (hidden file input) */}
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
                                    <Button variant="outlined" component="span" size="small" className="toggle-btn">
                                        {t("Upload .txt")}
                                    </Button>
                                </label>
                            </>
                        )}
                    </div>
                </div>

                {/* Display error alert if present */}
                {error && <Alert severity="error">{error}</Alert>}

                {/* Textarea for grammar input or transformed view */}
                <textarea
                    className="grammar-input"
                    value={localGrammar}
                    onChange={readOnly ? undefined : handleGrammarChange}
                    placeholder={t("grammarExamplePlaceholder")}
                    rows={10}
                    readOnly={readOnly}
                />
            </>
        );
    }
);

export default GrammarInput;
