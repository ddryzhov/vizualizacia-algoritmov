import React from "react";
import { useTranslation } from "react-i18next";
import { ButtonGroup, Button } from "@mui/material";
import "../../assets/styles/switcher/LanguageSwitcher.css";

const LanguageSwitcher = () => {
    const { i18n } = useTranslation();

    return (
        <div className="language-switcher">
            <ButtonGroup size="small" variant="outlined">
                <Button onClick={() => i18n.changeLanguage("en")} disabled={i18n.language === "en"}>
                    EN
                </Button>
                <Button onClick={() => i18n.changeLanguage("sk")} disabled={i18n.language === "sk"}>
                    SK
                </Button>
            </ButtonGroup>
        </div>
    );
};

export default LanguageSwitcher;
