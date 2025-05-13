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

/**
 * Component responsible for computing PREDICT sets for each production in a grammar.
 * Combines FIRST sets of production bodies and FOLLOW sets of non-terminals when ε is present.
 */
@Component
public class PredictSetCalculator {
    /**
     * Computes PREDICT sets and records each pseudocode step for visualization.
     *
     * @param productionRules map of non-terminals to their production alternatives
     * @param firstSets       precomputed FIRST sets for lookahead
     * @param followSets      precomputed FOLLOW sets for ε propagation
     * @param grammar         Grammar model to populate with PREDICT sets and step records
     */
    public void computePredictSets(
            Map<String, List<String>> productionRules,
            Map<String, Set<String>> firstSets,
            Map<String, Set<String>> followSets,
            Grammar grammar
    ) {
        // Initialize storage for PREDICT sets and step records
        Map<String, Set<String>> predictSets = new LinkedHashMap<>();
        List<StepRecord> steps = new ArrayList<>();

        // Step 0: Begin PREDICT computation
        SetUtils.recordStep("Line 0: Start computing PREDICT sets",
                SetUtils.copySets(predictSets), steps, 0);

        // Iterate through each production
        for (String nonTerminal : productionRules.keySet()) {
            for (String production : productionRules.get(nonTerminal)) {
                String key = nonTerminal + " -> " + production;

                // Step 1: Calculate FIRST(α) for the production body
                SetUtils.recordStep("Line 1: Compute FIRST(α) for " + key,
                        SetUtils.copySets(predictSets), steps, 1);

                // Split production or handle ε
                String[] alpha = production.equals("epsilon") ? new String[]{"epsilon"}
                        : production.split("\\s+");
                Set<String> firstAlpha = GrammarUtils.computeFirstOfAlpha(alpha,
                        firstSets);

                // Step 2: If ε in FIRST(α), combine FIRST\{ε} with FOLLOW(nonTerminal)
                if (firstAlpha.contains("ε")) {
                    SetUtils.recordStep("Line 2: ε ∈ FIRST(α), do sub-steps 2a, 2b",
                            SetUtils.copySets(predictSets), steps, 2);

                    // Step 2a: Remove ε from FIRST(α)
                    Set<String> withoutEps = new LinkedHashSet<>(firstAlpha);
                    withoutEps.remove("ε");
                    SetUtils.recordStep("Line 2a: (firstAlpha \\ {ε}) = " + withoutEps,
                            SetUtils.copySets(predictSets), steps, 2);

                    // Step 2b: Union with FOLLOW(nonTerminal)
                    Set<String> combined = new LinkedHashSet<>(withoutEps);
                    combined.addAll(followSets.get(nonTerminal));
                    SetUtils.recordStep("Line 2b: PREDICT(" + key + ") = " + combined
                                    + " = (firstAlpha\\{ε}) ∪ FOLLOW(" + nonTerminal + ")",
                            SetUtils.copySets(predictSets), steps, 2);

                    predictSets.put(key, combined);
                } else {
                    // Step 3: ε not in FIRST(α); PREDICT = FIRST(α)
                    SetUtils.recordStep("Line 3: ε ∉ FIRST(α) => PREDICT("
                                    + key + ") = " + firstAlpha,
                            SetUtils.copySets(predictSets), steps, 3);
                    predictSets.put(key, firstAlpha);
                }
            }
        }

        // Step 4: Finalize PREDICT set computation
        SetUtils.recordStep("Line 4: Done computing PREDICT sets",
                SetUtils.copySets(predictSets), steps, 4);

        // Persist results in grammar model
        grammar.setPredictSets(predictSets);
        grammar.setPredictStepRecords(steps);
    }
}
