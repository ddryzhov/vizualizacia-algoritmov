package grammar.analyzer.grammarvisualizer.service.impl;

import grammar.analyzer.grammarvisualizer.model.Grammar;
import grammar.analyzer.grammarvisualizer.service.LL1Service;
import grammar.analyzer.grammarvisualizer.util.LL1TableUtils;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class Ll1ServiceImpl implements LL1Service {
    @Override
    public void buildLL1TableWithSteps(
            Map<String, List<String>> productionRules,
            Map<String, Set<String>> predictSets,
            Grammar grammar
    ) {
        // Initialize the LL(1) table
        Map<String, Map<String, String>> ll1Table = LL1TableUtils
                .initializeLl1Table(productionRules);
        boolean isLL1 = true; // Flag to track LL(1) validity
        StringBuilder description = new StringBuilder("LL(1) table is built "
                + "using PREDICT sets:\n\n");

        // Populate the LL(1) table
        for (String nonTerminal : productionRules.keySet()) {
            description.append("For non-terminal ").append(nonTerminal).append(":\n");
            for (String production : productionRules.get(nonTerminal)) {
                Set<String> predictSet = predictSets.get(nonTerminal + " -> " + production);
                description.append("  Rule [").append(nonTerminal).append(" -> ")
                        .append(production).append("], PREDICT = ").append(predictSet).append("\n");

                for (String terminal : predictSet) {
                    String existingRule = ll1Table.get(nonTerminal).get(terminal);
                    if (!existingRule.isEmpty()) {
                        isLL1 = false;
                        ll1Table.get(nonTerminal).put(
                                terminal,
                                existingRule + ", " + nonTerminal + " -> " + production
                        );
                    } else {
                        ll1Table.get(nonTerminal).put(terminal, nonTerminal + " -> " + production);
                    }
                }
            }
            description.append("\n");
        }

        // Store the results in the grammar object
        grammar.setLl1Table(ll1Table);
        grammar.setLl1(isLL1);
        grammar.setLl1Description(description.toString());
    }
}
