package grammar.analyzer.grammarvisualizer.service.calculators;

import grammar.analyzer.grammarvisualizer.model.Grammar;
import grammar.analyzer.grammarvisualizer.model.StepRecord;
import grammar.analyzer.grammarvisualizer.util.GrammarUtils;
import grammar.analyzer.grammarvisualizer.util.SetUtils;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class PredictSetCalculator {
    /**
     * Compute PREDICT sets for the given grammar.
     * This method calculates the PREDICT sets for all productions in the grammar by combining
     * FIRST sets of the production body and FOLLOW sets of the non-terminal when needed.
     *
     * @param productionRules the production rules of the grammar
     * @param firstSets       the computed FIRST sets for the grammar
     * @param followSets      the computed FOLLOW sets for the grammar
     * @param grammar         the grammar object to store the results
     */
    public void computePredictSets(
            Map<String, List<String>> productionRules,
            Map<String, Set<String>> firstSets,
            Map<String, Set<String>> followSets,
            Grammar grammar
    ) {
        // Initialize an empty map to store PREDICT sets
        Map<String, Set<String>> predictSets = new LinkedHashMap<>();
        List<StepRecord> steps = new ArrayList<>();

        // Step 0: Start computing PREDICT sets
        SetUtils.recordStep("Line 0: Start computing PREDICT sets",
                SetUtils.copySets(predictSets), steps, 0);

        // Iterate over all non-terminals and their productions
        for (String nonTerminal : productionRules.keySet()) {
            for (String production : productionRules.get(nonTerminal)) {
                String key = nonTerminal + " -> " + production;

                // Step 1: Compute FIRST(α) for the production body
                SetUtils.recordStep("Line 1: Compute FIRST(α) for " + key,
                        SetUtils.copySets(predictSets), steps, 1);

                // Handle epsilon or split the production into symbols
                String[] alpha = production.equals("epsilon") ? new String[]{"epsilon"}
                        : production.split("\\s+");
                Set<String> firstAlpha = GrammarUtils.computeFirstOfAlpha(alpha,
                        firstSets);

                // Step 2: If ε ∈ FIRST(α), add FOLLOW(nonTerminal) to the PREDICT set
                if (firstAlpha.contains("ε")) {
                    SetUtils.recordStep("Line 2: ε ∈ FIRST(α), do sub-steps 2a, 2b",
                            SetUtils.copySets(predictSets), steps, 2);

                    // Step 2a: Remove ε from FIRST(α)
                    Set<String> withoutEps = new LinkedHashSet<>(firstAlpha);
                    withoutEps.remove("ε");
                    SetUtils.recordStep("Line 2a: (firstAlpha \\ {ε}) = " + withoutEps,
                            SetUtils.copySets(predictSets), steps, 2);

                    // Step 2b: Combine (FIRST(α) \ {ε}) with FOLLOW(nonTerminal)
                    Set<String> combined = new LinkedHashSet<>(withoutEps);
                    combined.addAll(followSets.get(nonTerminal));
                    SetUtils.recordStep("Line 2b: PREDICT(" + key + ") = " + combined
                                    + " = (firstAlpha\\{ε}) ∪ FOLLOW(" + nonTerminal + ")",
                            SetUtils.copySets(predictSets), steps, 2);

                    predictSets.put(key, combined);
                } else {
                    // Step 3: If ε ∉ FIRST(α), the PREDICT set is simply FIRST(α)
                    SetUtils.recordStep("Line 3: ε ∉ FIRST(α) => PREDICT("
                                    + key + ") = " + firstAlpha,
                            SetUtils.copySets(predictSets), steps, 3);
                    predictSets.put(key, firstAlpha);
                }
            }
        }

        // Step 4: Finalize the computation of PREDICT sets
        SetUtils.recordStep("Line 4: Done computing PREDICT sets",
                SetUtils.copySets(predictSets), steps, 4);

        // Store results in the grammar object
        grammar.setPredictSets(predictSets);
        grammar.setPredictStepRecords(steps);
    }
}
