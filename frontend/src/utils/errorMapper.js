/**
 * Maps raw backend error messages to user-friendly, translatable strings.
 * Recognizes specific syntax error patterns and extracts context (e.g., rule text).
 * Falls back to a generic analysis error translation if no pattern matches.
 *
 * @param {string} rawMessage - The error message returned by the backend.
 * @param {Function} t - i18n translation function (from react-i18next).
 * @returns {string} Translated, user-friendly error message.
 */
export const mapBackendErrorToTranslation = (rawMessage, t) => {
    if (!rawMessage) return t("analysisError");

    if (rawMessage.startsWith("Invalid syntax: each rule must contain '->'")) {
        const rule = rawMessage.split("Rule: ")[1];
        return t("errorRequiredArrow", { rule });
    }
    if (rawMessage.startsWith("Invalid syntax: exactly one '->'")) {
        const rule = rawMessage.split("Rule: ")[1];
        return t("errorSingleArrow", { rule });
    }
    if (rawMessage.startsWith("Invalid syntax: left-hand side")) {
        const rule = rawMessage.split("Rule: ")[1];
        return t("errorInvalidLHS", { rule });
    }
    if (rawMessage.startsWith("Empty alternative is not allowed")) {
        const rule = rawMessage.split("Rule: ")[1];
        return t("errorEmptyAlternative", { rule });
    }
    if (rawMessage.startsWith("Undefined non-terminal(s):")) {
        const terms = rawMessage.split(": ")[1];
        return t("undefinedNonTerminals", { terms });
    }

    return rawMessage || t("analysisError");
};
