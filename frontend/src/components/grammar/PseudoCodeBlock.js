import React, { useRef, useEffect, useState } from "react";
import { Paper, Box } from "@mui/material";
import { MathJax, MathJaxContext } from "better-react-mathjax";

/**
 * Renders pseudo-code lines with highlighting and math rendering.
 */
const PseudoCodeBlock = React.memo(({ pseudoCodeLines, pseudoCodeLine }) => {
    const mathJaxRef = useRef(null);
    const [isRendered, setIsRendered] = useState(false);

    useEffect(() => {
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
            <Paper
                className="pseudo-code"
                style={{ opacity: isRendered ? 1 : 0, transition: "opacity 0.2s ease-in-out" }}
            >
                {pseudoCodeLines.map((line, index) => (
                    <Box
                        ref={index === pseudoCodeLine ? mathJaxRef : null}
                        key={index}
                        className={index === pseudoCodeLine ? "highlighted" : ""}
                    >
                        <MathJax>{`\\( ${line} \\)`}</MathJax>
                    </Box>
                ))}
            </Paper>
        </MathJaxContext>
    );
});

export default PseudoCodeBlock;
