/**
 * Mapping from analysis types to arrays of pseudocode lines.
 * Used by PseudoCodeBlock to render algorithm steps with MathJax formatting.
 * Keys: FIRST, FOLLOW, PREDICT.
 */
export const pseudoCodeMapping = {
    FIRST: [
        "1. \\textbf{if } \\alpha = \\varepsilon \\Rightarrow FIRST(\\alpha) = \\{ \\varepsilon \\}",
        "2. \\textbf{if } \\alpha = a\\beta,\\ a \\in T \\land \\beta \\in (N \\cup T)^* \\Rightarrow FIRST(\\alpha) = \\{ a \\}",
        "3. \\textbf{if } \\alpha = A\\beta,\\ A \\in N \\land \\beta \\in (N \\cup T)^* \\Rightarrow",
        "4. \\quad FSTA \\gets \\emptyset",
        "5. \\quad \\textbf{for each } A \\rightarrow \\gamma \\in P \\textbf{ do}",
        "6. \\quad\\quad \\textbf{if } A \\text{ is not a prefix of } \\gamma \\textbf{ then}",
        "7. \\quad\\quad\\quad FSTA \\gets FSTA \\cup FIRST(\\gamma)",
        "8. \\quad\\quad \\textbf{end if}",
        "9. \\quad \\textbf{end for}",
        "10. \\quad \\textbf{if } \\varepsilon \\in FSTA \\textbf{ then}",
        "11. \\quad\\quad FSTA \\gets (FSTA \\setminus \\{ \\varepsilon \\}) \\cup FIRST(\\beta)",
        "12. \\quad \\textbf{end if}",
        "13. \\quad \\textbf{return } FSTA",
        "14. \\textbf{end if}",
        "15: \\textbf{FIRST sets stabilized}"
    ],
    FOLLOW: [
        "1. FLW_{A} \\gets \\emptyset",
        "2. \\textbf{if } A = S \\textbf{ then}",
        "3. \\quad FLW_{A} \\gets \\{\\$\\}",
        "4. \\textbf{end if}",
        "5. \\textbf{for each } B \\rightarrow \\alpha A \\beta \\in P,\\ \\alpha, \\beta \\in (N \\cup T)^* \\textbf{ do}",
        "6. \\quad FLW_{A} \\gets FLW_{A} \\cup \\left(FIRST(\\beta) \\setminus \\{ \\varepsilon \\}\\right)",
        "7. \\quad \\textbf{if } \\varepsilon \\in FIRST(\\beta) \\textbf{ then}",
        "8. \\quad\\quad FLW_{A} \\gets FLW_{A} \\cup FOLLOW(B)",
        "9. \\quad \\textbf{end if}",
        "10. \\textbf{end for}",
        "11. \\textbf{return } FLW_{A}"
    ],
    PREDICT: [
        "1. \\text{Compute } FIRST(\\alpha)",
        "2. \\text{If } \\varepsilon \\in FIRST(\\alpha), \\text{ remove } \\varepsilon \\text{ and add } FOLLOW(A)",
        "3. \\text{Otherwise, } PREDICT(A \\to \\alpha) = FIRST(\\alpha)",
        "4. \\text{Done}"
    ]
};
