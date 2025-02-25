package grammar.analyzer.grammarvisualizer.service.calculators;

import grammar.analyzer.grammarvisualizer.model.Grammar;
import grammar.analyzer.grammarvisualizer.model.StepRecord;
import grammar.analyzer.grammarvisualizer.util.GrammarUtils;
import grammar.analyzer.grammarvisualizer.util.SetUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class FollowSetCalculator {
    /**
     * Compute FOLLOW sets for the given grammar.
     * This method calculates FOLLOW sets for all non-terminals by iteratively processing
     * production rules until the sets stabilize (no more changes occur).
     *
     * @param productionRules the production rules of the grammar
     * @param firstSets       the computed FIRST sets for the grammar
     * @param nonTerminals    the set of non-terminal symbols
     * @param startSymbol     the start symbol of the grammar
     * @param grammar         the grammar object to store the results
     */
    public void computeFollowSets(
            Map<String, List<String>> productionRules,
            Map<String, Set<String>> firstSets,
            Set<String> nonTerminals,
            String startSymbol,
            Grammar grammar
    ) {
        // Initialize FOLLOW sets for all non-terminals as empty sets
        Map<String, Set<String>> followSets = SetUtils.initializeEmptySets(nonTerminals);
        List<StepRecord> steps = new ArrayList<>();

        // Step 1: Initialize FOLLOW sets
        SetUtils.recordStep("Line 1: FOLLOW(A)=∅ for all A",
                SetUtils.copySets(followSets), steps, 1);

        // Step 2: Add `$` to FOLLOW(startSymbol)
        followSets.get(startSymbol).add("$");
        SetUtils.recordStep("Line 2: FOLLOW(" + startSymbol + ") += { $ }",
                SetUtils.copySets(followSets), steps, 2);

        boolean changed;
        int pass = 0;

        // Repeat until no changes occur
        do {
            pass++;
            SetUtils.recordStep("Line 3: Pass #" + pass + ", repeat until no more changes",
                    SetUtils.copySets(followSets), steps, 3);

            changed = false;

            // Iterate over all production rules (nonTerminal -> productions)
            for (String nonTerminal : productionRules.keySet()) {
                for (String production : productionRules.get(nonTerminal)) {
                    String[] symbols = production.split("\\s+");

                    // Iterate over each symbol in the production
                    for (int i = 0; i < symbols.length; i++) {
                        String symbol = symbols[i];

                        // Skip if the current symbol is not a non-terminal
                        if (!followSets.containsKey(symbol)) {
                            continue;
                        }

                        // Case 3.1: Handle symbols followed by others
                        if (i + 1 < symbols.length) {
                            // Compute FIRST(β) for the sequence β after the current symbol
                            String[] beta = Arrays.copyOfRange(symbols, i + 1, symbols.length);
                            Set<String> firstBeta = GrammarUtils.computeFirstOfAlpha(beta,
                                    firstSets, followSets.keySet());
                            SetUtils.recordStep("Line 4: (3.1a) FIRST(β)=" + firstBeta + " for β="
                                            + String.join(" ", beta),
                                    SetUtils.copySets(followSets), steps, 4);

                            // Add FIRST(β) \ {ε} to FOLLOW(symbol)
                            Set<String> noEps = new LinkedHashSet<>(firstBeta);
                            noEps.remove("ε");
                            if (!noEps.isEmpty()) {
                                boolean updated = followSets.get(symbol).addAll(noEps);
                                if (updated) {
                                    changed = true;
                                    SetUtils.recordStep("Line 5: (3.1b) FOLLOW(" + symbol
                                                    + ") += " + noEps,
                                            SetUtils.copySets(followSets), steps, 5);
                                }
                            }

                            // If ε ∈ FIRST(β), add FOLLOW(nonTerminal) to FOLLOW(symbol)
                            if (firstBeta.contains("ε")) {
                                boolean upd2 = followSets.get(symbol)
                                        .addAll(followSets.get(nonTerminal));
                                if (upd2) {
                                    changed = true;
                                    SetUtils.recordStep("Line 6: (3.1c) ε ∈ FIRST(β), FOLLOW("
                                                    + symbol + ") += FOLLOW(" + nonTerminal + ")",
                                            SetUtils.copySets(followSets), steps, 6);
                                }
                            }
                        } else {
                            // Case 3.2: If the symbol is the last in the production,
                            // add FOLLOW(nonTerminal)
                            boolean upd3 = followSets.get(symbol)
                                    .addAll(followSets.get(nonTerminal));
                            if (upd3) {
                                changed = true;
                                SetUtils.recordStep("Line 7: (3.2) B is last => FOLLOW(" + symbol
                                                + ") += FOLLOW(" + nonTerminal + ")",
                                        SetUtils.copySets(followSets), steps, 7);
                            }
                        }
                    }
                }
            }
        } while (changed);

        // Record the final state of FOLLOW sets
        SetUtils.recordStep("Line 8: FOLLOW sets stabilized after pass #" + pass,
                SetUtils.copySets(followSets), steps, 8);

        // Store results in the grammar object
        grammar.setFollowSets(followSets);
        grammar.setFollowStepRecords(steps);
    }
}
