import React from "react";
import { useTranslation } from "react-i18next";
import { ButtonGroup, Button } from "@mui/material";
import "../../assets/styles/switcher/LanguageSwitcher.css";

/**
 * LanguageSwitcher component renders buttons to switch the app language between English and Slovak.
 * Disables the button for the currently active language.
 */
const LanguageSwitcher = () => {
    const { i18n } = useTranslation(); // i18n instance for language operations

    return (
        <div className="language-switcher">
            {/* Group of language toggle buttons */}
            <ButtonGroup size="small" variant="outlined">
                {/* English button: sets language to 'en' */}
                <Button onClick={() => i18n.changeLanguage("en")} disabled={i18n.language === "en"}>
                    EN
                </Button>
                {/* Slovak button: sets language to 'sk' */}
                <Button onClick={() => i18n.changeLanguage("sk")} disabled={i18n.language === "sk"}>
                    SK
                </Button>
            </ButtonGroup>
        </div>
    );
};

export default LanguageSwitcher;
