package grammar.analyzer.grammarvisualizer.service.calculators;

import grammar.analyzer.grammarvisualizer.model.Grammar;
import grammar.analyzer.grammarvisualizer.model.StepRecord;
import grammar.analyzer.grammarvisualizer.util.GrammarUtils;
import grammar.analyzer.grammarvisualizer.util.SetUtils;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class FirstSetCalculator {
    /**
     * Compute FIRST sets for the given grammar.
     * This method iteratively calculates the FIRST sets for all non-terminals
     * until the sets stabilize (no more changes occur).
     *
     * @param productionRules the production rules of the grammar
     * @param nonTerminals    the set of non-terminal symbols
     * @param grammar         the grammar object to store the results
     */
    public void computeFirstSets(
            Map<String, List<String>> productionRules,
            Set<String> nonTerminals,
            Grammar grammar
    ) {
        // Initialize FIRST sets for all non-terminals as empty sets
        Map<String, Set<String>> firstSets = SetUtils.initializeEmptySets(nonTerminals);
        List<StepRecord> steps = new ArrayList<>();

        // Record initial state of FIRST sets
        SetUtils.recordStep("Line 0: Initialize FIRST sets = ∅",
                SetUtils.copySets(firstSets), steps, 0);

        boolean changed;
        do {
            changed = false;
            // Iterate over all production rules (nonTerminal -> productions)
            for (String nonTerminal : productionRules.keySet()) {
                for (String production : productionRules.get(nonTerminal)) {
                    Map<String, Set<String>> before = SetUtils.copySets(firstSets);

                    // Compute FIRST set for the specific production
                    computeFirstForProduction(nonTerminal, production, firstSets,
                            nonTerminals, steps);

                    // Check if any changes occurred in the FIRST sets
                    if (!SetUtils.equalsSets(before, firstSets)) {
                        changed = true;
                    }
                }
            }
        } while (changed); // Repeat until no changes occur

        // Record the final state of FIRST sets
        SetUtils.recordStep("Line 5: FIRST sets stabilized (no more changes)",
                SetUtils.copySets(firstSets), steps, 5);

        // Step 6: Mark the completion of the computation
        SetUtils.recordStep("Line 6: Done computing FIRST sets",
                SetUtils.copySets(firstSets), steps, 6);

        // Store results in the grammar object
        grammar.setFirstSets(firstSets);
        grammar.setFirstStepRecords(steps);
    }

    /**
     * Compute the FIRST set for a specific production of a non-terminal.
     *
     * @param nonTerminal  the non-terminal symbol
     * @param production   the production (right-hand side)
     * @param firstSets    the map containing FIRST sets for all non-terminals
     * @param nonTerminals the set of non-terminal symbols
     * @param steps        the list of steps for recording intermediate results
     */
    private void computeFirstForProduction(
            String nonTerminal,
            String production,
            Map<String, Set<String>> firstSets,
            Set<String> nonTerminals,
            List<StepRecord> steps
    ) {
        // Case 1: If the production is epsilon, add epsilon to the FIRST set
        if ("epsilon".equals(production)) {
            firstSets.get(nonTerminal).add("ε");
            SetUtils.recordStep("Line 1: Production is ε => FIRST(" + nonTerminal + ") += {ε}",
                    SetUtils.copySets(firstSets), steps, 1);
            return;
        }

        // Split the production into symbols (e.g., "A B C" -> [A, B, C])
        String[] symbols = production.split("\\s+");
        SetUtils.recordStep("Line 2: Splitting " + nonTerminal + " → " + production,
                SetUtils.copySets(firstSets), steps, 2);

        // Compute FIRST(X1) for the first symbol in the production
        Set<String> firstSetX1 = GrammarUtils.computeFirstOfSymbol(symbols[0],
                firstSets, nonTerminals);
        Set<String> withoutEps = new LinkedHashSet<>(firstSetX1);
        withoutEps.remove("ε");

        // Start forming FIRST(α)
        Set<String> alphaFirst = new LinkedHashSet<>(withoutEps);
        SetUtils.recordStep("Line 2: FIRST(" + nonTerminal + ") += FIRST("
                        + symbols[0] + ")\\{ε} = " + withoutEps,
                SetUtils.copySets(firstSets), steps, 2);

        int i = 1;
        // Iterate while epsilon is in FIRST(Xi) and there are more symbols
        while (i < symbols.length && firstSetX1.contains("ε")) {
            SetUtils.recordStep("Line 3: ε ∈ FIRST(" + symbols[i - 1]
                            + "), moving to " + symbols[i],
                    SetUtils.copySets(firstSets), steps, 3);

            firstSetX1 = GrammarUtils.computeFirstOfSymbol(symbols[i], firstSets, nonTerminals);
            Set<String> tempSet = new LinkedHashSet<>(firstSetX1);
            tempSet.remove("ε");
            alphaFirst.addAll(tempSet);

            SetUtils.recordStep("Line 3: Add FIRST(" + symbols[i] + ")\\{ε} = "
                            + tempSet + " to FIRST(" + nonTerminal + ")",
                    SetUtils.copySets(firstSets), steps, 3);
            i++;
        }

        // Case 4: If ε ∈ FIRST(Xn) and i = n, add ε to FIRST(α)
        if (i == symbols.length && firstSetX1.contains("ε")) {
            alphaFirst.add("ε");
            SetUtils.recordStep("Line 4: i=n & ε ∈ FIRST(" + symbols[i - 1] + "), so add ε",
                    SetUtils.copySets(firstSets), steps, 4);
        }

        // Update FIRST(nonTerminal) and record changes
        boolean changed = firstSets.get(nonTerminal).addAll(alphaFirst);
        if (changed) {
            SetUtils.recordStep("Updated FIRST(" + nonTerminal + ") = "
                            + firstSets.get(nonTerminal),
                    SetUtils.copySets(firstSets), steps, 5);
        }
    }
}
