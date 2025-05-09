import React, { useState } from "react";
import {
    Dialog,
    DialogTitle,
    DialogContent,
    IconButton,
    Typography,
    Tabs,
    Tab,
    Box,
} from "@mui/material";
import HelpOutlineIcon from "@mui/icons-material/HelpOutline";
import CloseIcon from "@mui/icons-material/Close";
import "../../assets/styles/help/HelpDialog.css";
import {useTranslation} from "react-i18next";

const TabPanel = ({ children, value, index }) => (
    <div role="tabpanel" hidden={value !== index}>
        {value === index && <Box sx={{ p: 2 }}>{children}</Box>}
    </div>
);

// HelpDialog component provides user guidance through a tabbed dialog window
const HelpDialog = () => {
    const [open, setOpen] = useState(false);
    const [tab, setTab] = useState(0);
    const { t } = useTranslation();

    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);
    const handleTabChange = (_, newTab) => setTab(newTab);

    return (
        <>
            <IconButton className="help-icon" onClick={handleOpen}>
                <HelpOutlineIcon />
            </IconButton>

            <Dialog open={open} onClose={handleClose} fullWidth maxWidth="md">
                <DialogTitle>
                    {t("Help")}
                    <IconButton
                        aria-label="close"
                        onClick={handleClose}
                        sx={{ position: "absolute", right: 8, top: 8 }}
                    >
                        <CloseIcon />
                    </IconButton>
                </DialogTitle>

                <DialogContent>
                    <Tabs value={tab} onChange={handleTabChange}>
                        <Tab label={t("General")} />
                        <Tab label={t("Grammar Syntax")} />
                        <Tab label={t("Special Symbols")} />
                    </Tabs>

                    <TabPanel value={tab} index={0}>
                        <Typography variant="h6">About Application</Typography>
                        <Typography variant="body1">
                            {t("This web application visualizes grammar analysis using the FIRST, FOLLOW, PREDICT algorithms and checks LL(1) compliance. You can input grammars in plain or EBNF format. Step-by-step visualization helps in educational understanding of parsing algorithms.")}
                        </Typography>

                        <Typography variant="h6" sx={{mt: 2}}>
                            {t("Controls")}
                        </Typography>
                        <ul>
                            <li><strong>FIRST, FOLLOW, PREDICT, LL1:</strong> {t("Switch analysis types")}</li>
                            <li><strong>PREV/NEXT:</strong> {t("Navigate through algorithm steps")}</li>
                            <li><strong>RESET:</strong> {t("Return to the first step")}</li>
                            <li><strong>RESULT:</strong> {t("Show the final step result")}</li>
                        </ul>

                        <Typography variant="h6" sx={{mt: 3}}>
                            {t("Generated non-terminals from EBNF")}
                        </Typography>
                        <Typography variant="body2">
                            {t("When you write grammar using EBNF syntax (e.g. with brackets, braces, or parentheses), the application internally transforms it into regular BNF format. This creates helper non-terminals such as _opt1, _rep2, or _alt3. These represent parts of the original EBNF rule.")}
                        </Typography>
                        <ul>
                            <li><code>_optX</code> — {t("optional part (e.g. [expr])")}</li>
                            <li><code>_repX</code> — {t("repeated part (e.g. {expr})")}</li>
                            <li><code>_altX</code> — {t("alternatives (e.g. A | B)")}</li>
                        </ul>
                        <Typography variant="body2">
                            {t("These non-terminals are shown in the results and LL(1) table and help explain how your EBNF was processed.")}
                        </Typography>
                    </TabPanel>

                    <TabPanel value={tab} index={1}>
                        <Typography variant="h6">{t("Grammar Input Examples")}</Typography>
                        <Typography variant="body2">
                            {t("You can use standard or EBNF grammar syntax, for example:")}
                        </Typography>
                        <Box component="pre" className="code-block">
                            {`S -> 'a' A | 'b' B
A -> 'c' | epsilon
B -> 'd'

EBNF example:
S -> 'a' [ 'b' ] { 'c' | 'd' }`}
                        </Box>
                    </TabPanel>

                    <TabPanel value={tab} index={2}>
                        <Typography variant="h6">{t("Special Symbols (TeX notation)")}</Typography>
                        <table className="symbols-table">
                            <thead>
                            <tr>
                                <th>{t("TeX Notation")}</th>
                                <th>{t("ASCII Symbol")}</th>
                                <th>{t("Displayed as")}</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>\eps</td>
                                <td>epsilon</td>
                                <td>epsilon</td>
                            </tr>
                            <tr>
                                <td>\to</td>
                                <td>-&gt;</td>
                                <td>-></td>
                            </tr>
                            <tr>
                                <td>\mid</td>
                                <td>|</td>
                                <td>|</td>
                            </tr>
                            </tbody>
                        </table>
                    </TabPanel>
                </DialogContent>
            </Dialog>
        </>
    );
};

export default HelpDialog;
