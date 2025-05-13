package grammar.analyzer.grammarvisualizer.service.impl;

import grammar.analyzer.grammarvisualizer.exception.GrammarSyntaxException;
import grammar.analyzer.grammarvisualizer.service.EbnfTransformerService;
import grammar.analyzer.grammarvisualizer.service.GrammarParserService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service implementation for parsing grammar definitions into production maps.
 * Supports EBNF syntax by transforming constructs into BNF before parsing.
 */
@RequiredArgsConstructor
@Service
public class GrammarParserServiceImpl implements GrammarParserService {
    private final EbnfTransformerService ebnfTransformerService;

    /**
     * Parses a grammar input string into a map of non-terminals to their production lists.
     * Applies EBNF-to-BNF transformation if EBNF constructs are detected.
     * Validates syntax and throws GrammarSyntaxException on errors.
     *
     * @param grammarInput multiline string with grammar rules separated by newlines
     * @return ordered map of non-terminal keys and lists of production alternatives
     * @throws GrammarSyntaxException if grammar syntax is invalid or undefined non-terminals exist
     */
    @Override
    public Map<String, List<String>> parseGrammar(String grammarInput) {
        // Detect and transform EBNF constructs ({}, [], ()) to BNF
        if (grammarInput.contains("{") || grammarInput.contains("[")
                || grammarInput.contains("(")
        ) {
            grammarInput = ebnfTransformerService.transform(grammarInput);
        }

        // Normalize escape sequences and delimiter symbols
        grammarInput = grammarInput
                .replace("\\eps", "epsilon")
                .replace("\\to", "->")
                .replace("\\mid", "|");

        // Split input into individual rule lines
        String[] rules = grammarInput.split("\n");
        Map<String, List<String>> productionRules = new LinkedHashMap<>();

        // Process each rule line
        for (String rule : rules) {
            if (!rule.contains("->")) {
                throw new GrammarSyntaxException("Invalid syntax: each rule "
                        + "must contain '->'. Rule: " + rule);
            }

            String[] parts = rule.split("->");
            if (parts.length != 2) {
                throw new GrammarSyntaxException("Invalid syntax: exactly one '->' "
                        + "expected in each rule. Rule: " + rule);
            }

            // Extract and validate non-terminal on LHS
            String nonTerminal = parts[0].trim();
            if (nonTerminal.isEmpty() || !nonTerminal.matches("[A-Za-z_][A-Za-z0-9_']*")) {
                throw new GrammarSyntaxException("Invalid syntax: left-hand side of the rule "
                        + "must be a valid non-terminal (e.g., A, S', Expr). Rule: " + rule);
            }

            // Split RHS into alternatives and collect productions
            String[] alternatives = parts[1].trim().split("\\|", -1);
            List<String> currentProductions = productionRules
                    .computeIfAbsent(nonTerminal, k -> new ArrayList<>());

            for (String alternative : alternatives) {
                String trimmed = alternative.trim();
                if (trimmed.isEmpty()) {
                    throw new GrammarSyntaxException("Empty alternative is not allowed. "
                            + "Use 'epsilon' explicitly if needed");
                }
                currentProductions.add(trimmed);
            }
        }

        // Collect defined non-terminals and used symbols
        List<String> defined = new ArrayList<>(productionRules.keySet());
        List<String> used = new ArrayList<>();

        for (List<String> prods : productionRules.values()) {
            for (String prod : prods) {
                for (String sym : prod.trim().split("\\s+")) {
                    boolean isTerminal = sym.startsWith("'") && sym.endsWith("'");
                    boolean isEpsilon = "epsilon".equals(sym);
                    boolean isOperator = "|".equals(sym) || "->".equals(sym);
                    if (!isTerminal && !isEpsilon && !isOperator) {
                        used.add(sym);
                    }
                }
            }
        }

        // Identify any undefined non-terminals
        List<String> undefined = used.stream()
                .distinct()
                .filter(sym -> !defined.contains(sym))
                .toList();

        if (!undefined.isEmpty()) {
            throw new GrammarSyntaxException(
                    "Undefined non-terminal(s): " + String.join(", ", undefined)
            );
        }

        return productionRules;
    }
}
