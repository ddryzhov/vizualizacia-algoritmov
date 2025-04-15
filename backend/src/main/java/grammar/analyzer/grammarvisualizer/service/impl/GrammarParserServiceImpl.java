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

@RequiredArgsConstructor
@Service
public class GrammarParserServiceImpl implements GrammarParserService {
    private final EbnfTransformerService ebnfTransformerService;

    /**
     * Parse the given grammar input into a map of production rules.
     * This method takes a string representation of a grammar,
     * where each rule is separated by a newline,
     * and converts it into a map with non-terminals as keys
     * and their respective productions as values.
     *
     * The grammar syntax is validated, and any violations result in a GrammarSyntaxException.
     *
     * @param grammarInput the input string representing the grammar
     * @return a map where keys are non-terminals, and values are lists of productions
     * @throws GrammarSyntaxException if the input grammar has invalid syntax
     */
    @Override
    public Map<String, List<String>> parseGrammar(String grammarInput) {
        if (grammarInput.contains("{") || grammarInput.contains("[")
                || grammarInput.contains("(")
        ) {
            grammarInput = ebnfTransformerService.transform(grammarInput);
        }

        // Initialize a map to store production rules
        grammarInput = grammarInput
                .replace("\\eps", "epsilon")
                .replace("\\to", "->")
                .replace("\\mid", "|");

        // Split input into individual rules by newline
        String[] rules = grammarInput.split("\n");
        Map<String, List<String>> productionRules = new LinkedHashMap<>();

        // Iterate over each rule in the input
        for (String rule : rules) {
            // Ensure the rule contains the production operator "->"
            if (!rule.contains("->")) {
                throw new GrammarSyntaxException("Invalid syntax: each rule "
                        + "must contain '->'. Rule: " + rule);
            }

            // Split the rule into the left-hand side (non-terminal)
            // and right-hand side (productions)
            String[] parts = rule.split("->");
            if (parts.length != 2) {
                throw new GrammarSyntaxException("Invalid syntax: exactly one '->' "
                        + "expected in each rule. Rule: " + rule);
            }

            // Extract and validate the non-terminal on the left-hand side
            String nonTerminal = parts[0].trim();
            if (nonTerminal.isEmpty() || !nonTerminal.matches("[A-Za-z_][A-Za-z0-9_']*")) {
                throw new GrammarSyntaxException("Invalid syntax: left-hand side of the rule "
                        + "must be a valid non-terminal (e.g., A, S', Expr). Rule: " + rule);
            }

            // Split the right-hand side into alternatives by "|"
            String[] alternatives = parts[1].trim().split("\\|", -1);
            // Retrieve or initialize the list of productions for the current non-terminal
            List<String> currentProductions = productionRules
                    .computeIfAbsent(nonTerminal, k -> new ArrayList<>());

            // Process each alternative production
            for (String alternative : alternatives) {
                String trimmed = alternative.trim();
                if (trimmed.isEmpty()) {
                    throw new GrammarSyntaxException("Empty alternative is not allowed. "
                            + "Use 'epsilon' explicitly if needed");
                }
                currentProductions.add(trimmed);
            }
        }

        // Return the parsed production rules
        return productionRules;
    }
}
