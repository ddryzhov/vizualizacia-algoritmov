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

/**
 * TabPanel helper component to conditionally render tab content.
 * @param {object} props
 * @param {React.ReactNode} props.children - Content to display.
 * @param {number} props.value - Current selected tab index.
 * @param {number} props.index - Index of this panel.
 */
const TabPanel = ({ children, value, index }) => (
    <div role="tabpanel" hidden={value !== index}>
        {value === index && <Box sx={{ p: 2 }}>{children}</Box>}
    </div>
);

/**
 * HelpDialog component showing a multi-tab modal with user guidance.
 * Tabs include general info, grammar syntax rules, and special symbols.
 */
const HelpDialog = () => {
    const [open, setOpen] = useState(false);
    const [tab, setTab] = useState(0);
    const { t } = useTranslation();

    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);
    const handleTabChange = (_, newTab) => setTab(newTab);

    return (
        <>
            {/* Help icon button to trigger dialog */}
            <IconButton className="help-icon" onClick={handleOpen}>
                <HelpOutlineIcon />
            </IconButton>

            {/* Modal dialog containing help content */}
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
                        {/* Tabs for different help sections */}
                        <Tab label={t("General")} />
                        <Tab label={t("Grammar Syntax")} />
                        <Tab label={t("Special Symbols")} />
                    </Tabs>

                    {/* General info tab */}
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

                    {/* Grammar syntax rules tab */}
                    <TabPanel value={tab} index={1}>
                        <Typography variant="h6">{t("Grammar Input Rules")}</Typography>
                        <Typography variant="body2" sx={{ mb: 2 }}>
                            {t("When writing grammar, follow these basic rules to ensure correct parsing and visualization:")}
                        </Typography>

                        <ul>
                            <li>
                                <strong>{t("Non-terminals")}:</strong> {t("Write as capitalized names without quotes (e.g.")} <code>S</code>, <code>Expr</code>, <code>Factor</code>{t(").")}
                            </li>
                            <li>
                                <strong>{t("Terminals")}:</strong> {t("Wrap in single quotes (e.g.")} <code>'a'</code>, <code>'+'</code>, <code>'('</code>{t(").")}
                            </li>
                            <li>
                                <strong>{t("Separators")}:</strong> {t("Use exactly one space between all symbols (non-terminals, terminals, arrows, pipes, etc.).")}
                            </li>
                            <li>
                                <strong>{t("Arrows")}:</strong> <code>-></code> {t("denotes a production (use ASCII dash and angle bracket).")}
                            </li>
                            <li>
                                <strong>{t("Alternatives")}:</strong> <code>|</code> {t("is used to separate multiple production options.")}
                            </li>
                            <li>
                                <strong>{t("Empty string")}:</strong> <code>epsilon</code> {t("is used to represent the empty production.")}
                            </li>
                        </ul>

                        <Typography variant="h6" sx={{ mt: 3 }}>
                            {t("Correct Examples")}
                        </Typography>
                        <Box component="pre" className="code-block">
                            {`S -> 'a' A | 'b' B
A -> 'c' | epsilon
B -> 'd'`}
                        </Box>

                        <Typography variant="h6" sx={{ mt: 3 }}>
                            {t("EBNF Example")}
                        </Typography>
                        <Box component="pre" className="code-block">
                            {`S -> 'a' [ 'b' ] { 'c' | 'd' }`}
                        </Box>

                        <Typography variant="body2" sx={{ mt: 2 }}>
                            {t("All symbols must be separated by spaces, exactly as shown above. Incorrect spacing or missing quotes may lead to parser errors.")}
                        </Typography>
                    </TabPanel>

                    {/* Special symbols tab */}
                    <TabPanel value={tab} index={2}>
                        <Typography variant="h6">{t("Special Symbols (TeX notation)")}</Typography>

                        <Typography variant="body2" sx={{ mb: 2 }}>
                            {t("In some examples or outputs, special symbols are shown using TeX or ASCII notation. This allows for a clearer and more consistent representation of grammar rules. You can write grammars using plain symbols like -> or |, or their LaTeX-style equivalents shown below.")}
                        </Typography>

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
