export const pseudoCodeMapping = {
    FIRST: [
        "1. \\text{If } \\alpha = \\varepsilon \\Rightarrow FIRST(\\alpha) = \\{ \\varepsilon \\}",
        "2. \\text{Otherwise, split } \\alpha \\Rightarrow FIRST(\\alpha) += FIRST(X_1) \\setminus \\{ \\varepsilon \\}, i=1",
        "3. \\text{While } (\\varepsilon \\in FIRST(X_i) \\land i < n) \\Rightarrow i++ \\text{ and add } FIRST(X_i) \\setminus \\{ \\varepsilon \\}",
        "4. \\text{If } (i=n \\land \\varepsilon \\in FIRST(X_n)) \\Rightarrow \\text{ add } \\varepsilon",
        "5. \\text{FINAL FIRST sets computed}",
        "6. \\text{Done computing FIRST sets}"
    ],
    FOLLOW: [
        "1. \\text{Initialize } FOLLOW(A) = \\emptyset \\text{ for all } A",
        "2. \\text{FOLLOW(S) } \\cup= \\{ \\$ \\} \\quad \\text{(Start symbol gets end-of-input marker)}",
        "3. \\text{Repeat until no more changes:}",
        "4. \\quad (3.1a) \\quad \\text{Compute } FIRST(\\beta) \\text{ for each production } A \\to \\alpha B \\beta",
        "5. \\quad (3.1b) \\quad FOLLOW(B) \\cup= \\left(FIRST(\\beta) \\setminus \\{ \\varepsilon \\} \\right)",
        "6. \\quad (3.1c) \\quad \\text{If } \\varepsilon \\in FIRST(\\beta), FOLLOW(B) \\cup= FOLLOW(A)",
        "7. \\quad (3.2) \\quad \\text{If } B \\text{ is last in the production } A \\to \\alpha B, FOLLOW(B) \\cup= FOLLOW(A)",
        "8. \\text{Done}"
    ],
    PREDICT: [
        "1. \\text{Compute } FIRST(\\alpha)",
        "2. \\text{If } \\varepsilon \\in FIRST(\\alpha), \\text{ remove } \\varepsilon \\text{ and add } FOLLOW(A)",
        "3. \\text{Otherwise, } PREDICT(A \\to \\alpha) = FIRST(\\alpha)",
        "4. \\text{Done}"
    ]
};
