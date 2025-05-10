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
