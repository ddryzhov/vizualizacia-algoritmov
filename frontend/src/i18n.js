import i18n from "i18next";
import { initReactI18next } from "react-i18next";

// Translation resources for English and Slovak
const resources = {
    en: {
        translation: {
            "Grammar Analyzer": "Grammar Analyzer",
            "Enter Grammar": "Enter Grammar",
            "Show Original EBNF": "Show Original EBNF",
            "Show Transformed BNF": "Show Transformed BNF",
            "Result": "Result",
            "Step Details": "Step Details",
            "No data available": "No data available",
            "No details available": "No details available",
            "Grammar cannot be empty!": "Grammar cannot be empty!",
            "Analysis error. Check your grammar or try again.": "Analysis error. Check your grammar or try again.",
            "Toggle theme": "Toggle theme",
            "Help": "Help",
            "General": "General",
            "Grammar Syntax": "Grammar Syntax",
            "Special Symbols": "Special Symbols",
            "About Application": "About Application",
            "This web application visualizes grammar analysis using the FIRST, FOLLOW, PREDICT algorithms and checks LL(1) compliance. You can input grammars in plain or EBNF format. Step-by-step visualization helps in educational understanding of parsing algorithms.": "This web application visualizes grammar analysis using the FIRST, FOLLOW, PREDICT algorithms and checks LL(1) compliance. You can input grammars in plain or EBNF format. Step-by-step visualization helps in educational understanding of parsing algorithms.",
            "Controls": "Controls",
            "Switch analysis types": "Switch analysis types.",
            "Navigate through algorithm steps": "Navigate through algorithm steps.",
            "Return to the first step": "Return to the first step.",
            "Show the final step result": "Show the final step result.",
            "Generated non-terminals from EBNF": "Generated non-terminals from EBNF",
            "When you write grammar using EBNF syntax (e.g. with brackets, braces, or parentheses), the application internally transforms it into regular BNF format. This creates helper non-terminals such as _opt1, _rep2, or _alt3. These represent parts of the original EBNF rule.": "When you write grammar using EBNF syntax (e.g. with brackets, braces, or parentheses), the application internally transforms it into regular BNF format. This creates helper non-terminals such as _opt1, _rep2, or _alt3. These represent parts of the original EBNF rule.",
            "optional part (e.g. [expr])": "optional part (e.g. [expr])",
            "repeated part (e.g. {expr})": "repeated part (e.g. {expr})",
            "alternatives (e.g. A | B)": "alternatives (e.g. A | B)",
            "These non-terminals are shown in the results and LL(1) table and help explain how your EBNF was processed.": "These non-terminals are shown in the results and LL(1) table and help explain how your EBNF was processed.",
            "Grammar Input Examples": "Grammar Input Examples",
            "You can use standard or EBNF grammar syntax, for example:": "You can use standard or EBNF grammar syntax, for example:",
            "EBNF example": "EBNF example",
            "Special Symbols (TeX notation)": "Special Symbols (TeX notation)",
            "TeX Notation": "TeX Notation",
            "ASCII Symbol": "ASCII Symbol",
            "Displayed as": "Displayed as",
            "parseTableTitle": "Parse Table",
            "ll1Success": "Grammar is LL(1): No conflicts in the table.",
            "ll1Error": "Grammar is NOT LL(1): Conflicts found in the table.",
            "nonTerminal": "Non-terminal",
            "grammarRules": "Grammar Rules",
            "Upload .txt": "Upload .txt"
        }
    },
    sk: {
        translation: {
            "Grammar Analyzer": "Analyzátor gramatiky",
            "Enter Grammar": "Zadajte gramatiku",
            "Show Original EBNF": "Zobraziť pôvodnú EBNF",
            "Show Transformed BNF": "Zobraziť transformovanú BNF",
            "Result": "Výsledok",
            "Step Details": "Podrobnosti kroku",
            "No data available": "Žiadne dáta nie sú dostupné",
            "No details available": "Podrobnosti nie sú dostupné",
            "Grammar cannot be empty!": "Gramatika nemôže byť prázdna!",
            "Analysis error. Check your grammar or try again.": "Chyba analýzy. Skontrolujte gramatiku alebo skúste znova.",
            "Toggle theme": "Prepnúť tému",
            "Help": "Pomoc",
            "General": "Všeobecné",
            "Grammar Syntax": "Syntax gramatiky",
            "Special Symbols": "Špeciálne znaky",
            "About Application": "O aplikácii",
            "This web application visualizes grammar analysis using the FIRST, FOLLOW, PREDICT algorithms and checks LL(1) compliance. You can input grammars in plain or EBNF format. Step-by-step visualization helps in educational understanding of parsing algorithms.": "Táto webová aplikácia vizualizuje analýzu gramatík pomocou algoritmov FIRST, FOLLOW, PREDICT a kontroluje LL(1) vlastnosti. Umožňuje zadávať gramatiky v bežnom alebo EBNF formáte. Vizualizácia krokov pomáha lepšie pochopiť algoritmy parsovania.",
            "Controls": "Ovládanie",
            "Switch analysis types": "Prepnutie medzi typmi analýzy.",
            "Navigate through algorithm steps": "Prechádzanie krokmi algoritmu.",
            "Return to the first step": "Návrat na prvý krok.",
            "Show the final step result": "Zobraziť výsledok.",
            "Generated non-terminals from EBNF": "Generované neterminály z EBNF",
            "When you write grammar using EBNF syntax (e.g. with brackets, braces, or parentheses), the application internally transforms it into regular BNF format. This creates helper non-terminals such as _opt1, _rep2, or _alt3. These represent parts of the original EBNF rule.": "Pri použití EBNF syntaxe (napr. hranaté alebo zložené zátvorky) sa gramatika automaticky transformuje na bežný BNF formát. Pri tom vznikajú pomocné neterminály ako _opt1, _rep2 alebo _alt3. Tieto reprezentujú časti pôvodných EBNF pravidiel.",
            "optional part (e.g. [expr])": "voliteľná časť (napr. [výraz])",
            "repeated part (e.g. {expr})": "opakovaná časť (napr. {výraz})",
            "alternatives (e.g. A | B)": "alternatívy (napr. A | B)",
            "These non-terminals are shown in the results and LL(1) table and help explain how your EBNF was processed.": "Tieto pomocné neterminály sa zobrazujú vo výsledkoch aj v LL(1) tabuľke a pomáhajú pochopiť, ako bola vaša gramatika transformovaná.",
            "Grammar Input Examples": "Príklady vstupnej gramatiky",
            "You can use standard or EBNF grammar syntax, for example:": "Môžete použiť bežnú alebo EBNF syntax gramatiky, napríklad:",
            "EBNF example": "Príklad EBNF",
            "Special Symbols (TeX notation)": "Špeciálne znaky (TeX notácia)",
            "TeX Notation": "TeX notácia",
            "ASCII Symbol": "ASCII symbol",
            "Displayed as": "Zobrazené ako",
            "parseTableTitle": "Parsovacia tabuľka",
            "ll1Success": "Gramatika je LL(1): V tabuľke nie sú žiadne konflikty.",
            "ll1Error": "Gramatika nie je LL(1): V tabuľke sa nachádzajú konflikty.",
            "nonTerminal": "Neterminál",
            "grammarRules": "Pravidlá gramatiky",
            "Upload .txt": "Nahrať .txt súbor"
        }
    }
};

i18n.use(initReactI18next).init({
    resources,
    lng: "en",
    fallbackLng: "en",
    interpolation: {
        escapeValue: false
    }
});

export default i18n;
