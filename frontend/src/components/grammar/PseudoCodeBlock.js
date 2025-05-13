import React, { useRef, useEffect, useState } from "react";
import { Paper, Box } from "@mui/material";
import { MathJax, MathJaxContext } from "better-react-mathjax";

/**
 * Component to render pseudocode lines with MathJax support and highlighting.
 * Applies fade-in transition after MathJax typesetting completes.
 *
 * @param {string[]} pseudoCodeLines - Array of pseudocode lines (LaTeX math content).
 * @param {number} pseudoCodeLine - Index of the currently highlighted line.
 */
const PseudoCodeBlock = React.memo(({ pseudoCodeLines, pseudoCodeLine }) => {
    const mathJaxRef = useRef(null);
    const [isRendered, setIsRendered] = useState(false);

    useEffect(() => {
        // Trigger re-typesetting and fade-in when lines or highlight change
        let timeout = setTimeout(async () => {
            setIsRendered(false);
            if (window.MathJax && mathJaxRef.current) {
                try {
                    await window.MathJax.typesetPromise();
                } catch (e) {
                    console.warn("MathJax error:", e);
                }
                setIsRendered(true);
            }
        }, 100);

        return () => clearTimeout(timeout);
    }, [pseudoCodeLines, pseudoCodeLine]);

    return (
        <MathJaxContext>
            {/* Paper container with fade-in on typeset completion */}
            <Paper
                className="pseudo-code"
                style={{ opacity: isRendered ? 1 : 0, transition: "opacity 0.2s ease-in-out" }}
            >
                {pseudoCodeLines.map((line, index) => (
                    <Box
                        ref={index === pseudoCodeLine ? mathJaxRef : null} // Attach ref to highlighted line
                        key={index}
                        className={index === pseudoCodeLine ? "highlighted" : ""}
                    >
                        {/* Wrap each line in MathJax for inline math rendering */}
                        <MathJax>{`\\( ${line} \\)`}</MathJax>
                    </Box>
                ))}
            </Paper>
        </MathJaxContext>
    );
});

export default PseudoCodeBlock;
