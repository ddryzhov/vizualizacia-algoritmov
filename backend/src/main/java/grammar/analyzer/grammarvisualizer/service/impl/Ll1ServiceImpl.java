package grammar.analyzer.grammarvisualizer.service.impl;

import grammar.analyzer.grammarvisualizer.model.Grammar;
import grammar.analyzer.grammarvisualizer.service.LL1Service;
import grammar.analyzer.grammarvisualizer.util.LL1TableUtils;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 * Service implementation for building the LL(1) parsing table.
 */
@Service
public class Ll1ServiceImpl implements LL1Service {
    /**
     * Builds the LL(1) table and generates a descriptive log using the PREDICT sets.
     *
     * @param productionRules the grammar production rules
     * @param predictSets     the computed PREDICT sets for each production
     * @param grammar         the grammar object to store the LL(1) table and related info
     */
    @Override
    public void buildLL1TableWithSteps(
            Map<String, List<String>> productionRules,
            Map<String, Set<String>> predictSets,
            Grammar grammar
    ) {
        // Initialize the LL(1) table.
        Map<String, Map<String, String>> ll1Table = LL1TableUtils
                .initializeLl1Table(productionRules);
        boolean isLL1 = true;
        StringBuilder description = new StringBuilder("LL(1) table is built "
                + "using PREDICT sets:\n\n");

        // Process each non-terminal.
        for (String nonTerminal : productionRules.keySet()) {
            description.append("For non-terminal ").append(nonTerminal).append(":\n");
            for (String production : productionRules.get(nonTerminal)) {
                String rule = nonTerminal + " -> " + production;
                int ruleNumber = grammar.getProductionRuleNumbers().get(rule);
                String ruleLabel = "R" + ruleNumber;
                Set<String> predictSet = predictSets.get(rule);

                description.append("  ").append(ruleLabel).append(": ")
                        .append(rule).append(", PREDICT = ")
                        .append(predictSet).append("\n");

                // Update the LL(1) table for each terminal in the PREDICT set.
                for (String terminal : predictSet) {
                    String existing = ll1Table.get(nonTerminal).get(terminal);
                    if (!existing.isEmpty()) {
                        isLL1 = false;
                        ll1Table.get(nonTerminal).put(terminal, existing + ", " + ruleLabel);
                    } else {
                        ll1Table.get(nonTerminal).put(terminal, ruleLabel);
                    }
                }
            }
            description.append("\n");
        }

        // Set the LL(1) table and description in the grammar.
        grammar.setLl1Table(ll1Table);
        grammar.setLl1(isLL1);
        grammar.setLl1Description(description.toString());
    }
}
