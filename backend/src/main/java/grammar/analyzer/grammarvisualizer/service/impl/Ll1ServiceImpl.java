package grammar.analyzer.grammarvisualizer.service.impl;

import grammar.analyzer.grammarvisualizer.model.Grammar;
import grammar.analyzer.grammarvisualizer.service.LL1Service;
import grammar.analyzer.grammarvisualizer.util.LL1TableUtils;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 * Service implementation for constructing the LL(1) parsing table.
 */
@Service
public class Ll1ServiceImpl implements LL1Service {
    /**
     * Builds the LL(1) parse table based on PREDICT sets and production rule numbering.
     * Marks the grammar as LL(1) if no parsing conflicts are detected.
     *
     * @param productionRules map of non-terminals to their production alternatives
     * @param predictSets     map of production identifiers to their PREDICT sets
     * @param grammar         Grammar model to populate with LL(1) table and compliance flag
     */
    @Override
    public void buildLl1Table(
            Map<String, List<String>> productionRules,
            Map<String, Set<String>> predictSets,
            Grammar grammar
    ) {
        // Initialize table cells to empty strings
        Map<String, Map<String, String>> ll1Table = LL1TableUtils
                .initializeLl1Table(productionRules);
        boolean isLL1 = true;

        // Iterate over each production to fill table entries
        for (String nonTerminal : productionRules.keySet()) {
            for (String production : productionRules.get(nonTerminal)) {
                String rule = nonTerminal + " -> " + production;
                int ruleNumber = grammar.getProductionRuleNumbers().get(rule);
                String ruleLabel = "R" + ruleNumber;
                Set<String> predictSet = predictSets.get(rule);

                // For each terminal in PREDICT, assign or append the rule label
                for (String terminal : predictSet) {
                    String existing = ll1Table.get(nonTerminal).get(terminal);
                    if (!existing.isEmpty()) {
                        // Conflict: multiple rules predict the same terminal
                        isLL1 = false;
                        ll1Table.get(nonTerminal).put(terminal, existing + ", " + ruleLabel);
                    } else {
                        // No conflict: assign this rule label
                        ll1Table.get(nonTerminal).put(terminal, ruleLabel);
                    }
                }
            }
        }

        // Store the completed table and LL(1) status
        grammar.setLl1Table(ll1Table);
        grammar.setLl1(isLL1);
    }
}
