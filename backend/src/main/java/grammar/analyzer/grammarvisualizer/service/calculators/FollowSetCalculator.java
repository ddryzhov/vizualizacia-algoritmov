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

/**
 * Component responsible for computing FOLLOW sets for a given grammar.
 * Captures each step in StepRecord objects for visualization.
 */
@Component
public class FollowSetCalculator {
    /**
     * Computes and stores FOLLOW sets in the provided Grammar model.
     * Implements the standard iterative algorithm, recording each line of pseudocode.
     *
     * @param productionRules map of non-terminals to their production alternatives
     * @param firstSets       precomputed FIRST sets for lookahead
     * @param nonTerminals    set of all non-terminal symbols
     * @param startSymbol     the start symbol, to initialize FOLLOW(startSymbol) with $
     * @param grammar         Grammar model to populate with FOLLOW sets and step records
     */
    public void computeFollowSets(
            Map<String, List<String>> productionRules,
            Map<String, Set<String>> firstSets,
            Set<String> nonTerminals,
            String startSymbol,
            Grammar grammar
    ) {
        // Step 1: Initialize FOLLOW(A) = ∅ for all non-terminals
        Map<String, Set<String>> followSets = SetUtils.initializeEmptySets(nonTerminals);
        List<StepRecord> steps = new ArrayList<>();
        SetUtils.recordStep("Line 1: FOLLOW(A) = ∅ for all A (Initialize)",
                SetUtils.copySets(followSets), steps, 1);

        // Steps 2-4: Add endmarker $ to FOLLOW(startSymbol)
        followSets.get(startSymbol).add("$");
        SetUtils.recordStep("Line 2: if A = S then", SetUtils.copySets(followSets), steps, 2);
        SetUtils.recordStep("Line 3: FLW(" + startSymbol + ") ← { $ }",
                SetUtils.copySets(followSets), steps, 3);
        SetUtils.recordStep("Line 4: end if", SetUtils.copySets(followSets), steps, 4);

        boolean changed;
        int pass = 0;
        // Step 5: Repeat until no changes
        do {
            pass++;
            SetUtils.recordStep("Line 5: (Iteration #" + pass + ") Repeat until no changes",
                    SetUtils.copySets(followSets), steps, 5);

            changed = false;

            // Process each production for each non-terminal
            for (String lhs : productionRules.keySet()) {
                for (String production : productionRules.get(lhs)) {
                    String[] symbols = production.split("\\s+");
                    // Examine each symbol in the production
                    for (int i = 0; i < symbols.length; i++) {
                        String currentSymbol = symbols[i];
                        // Process only nonterminals (those with FOLLOW sets)
                        if (!followSets.containsKey(currentSymbol)) {
                            continue; // Skip terminals
                        }

                        // Case: symbol followed by β
                        if (i + 1 < symbols.length) {
                            String[] beta = Arrays.copyOfRange(symbols, i + 1, symbols.length);
                            Set<String> firstBeta = GrammarUtils.computeFirstOfAlpha(beta,
                                    firstSets);
                            SetUtils.recordStep("Line 6: Compute FIRST(β) for β = "
                                            + String.join(" ", beta)
                                            + " ⇒ " + firstBeta,
                                    SetUtils.copySets(followSets), steps, 6);

                            // Step 7: Add FIRST(β) \ {ε} to FOLLOW(sym)
                            Set<String> firstBetaNoEps = new LinkedHashSet<>(firstBeta);
                            firstBetaNoEps.remove("ε");
                            if (!firstBetaNoEps.isEmpty()) {
                                boolean updated = followSets.get(currentSymbol)
                                        .addAll(firstBetaNoEps);
                                if (updated) {
                                    changed = true;
                                    SetUtils.recordStep("Line 7: FLW(" + currentSymbol
                                                    + ") ← FLW(" + currentSymbol
                                                    + ") ∪ (FIRST(β) without ε) ⇒ "
                                                    + firstBetaNoEps,
                                            SetUtils.copySets(followSets), steps, 7);
                                }
                            }

                            // Step 8: If ε ∈ FIRST(β), add FOLLOW(lhs) to FOLLOW(sym)
                            SetUtils.recordStep("Line 8: if ε ∈ FIRST(β) then",
                                    SetUtils.copySets(followSets), steps, 8);
                            if (firstBeta.contains("ε")) {
                                boolean updated = followSets.get(currentSymbol)
                                        .addAll(followSets.get(lhs));
                                if (updated) {
                                    changed = true;
                                    SetUtils.recordStep(
                                            "Line 9: FLW(" + currentSymbol
                                                    + ") ← FLW(" + currentSymbol
                                                    + ") ∪ FOLLOW(" + lhs + ") ⇒ "
                                                    + followSets.get(lhs),
                                            SetUtils.copySets(followSets), steps, 9);
                                }
                            }
                            SetUtils.recordStep("Line 10: end if",
                                    SetUtils.copySets(followSets), steps, 10);
                        } else {
                            // Case: sym is last in production, add FOLLOW(lhs)
                            SetUtils.recordStep("Line 8: if " + currentSymbol
                                            + " is last in production (B → α A) then",
                                    SetUtils.copySets(followSets), steps, 8);
                            boolean updated = followSets.get(currentSymbol)
                                    .addAll(followSets.get(lhs));
                            if (updated) {
                                changed = true;
                                SetUtils.recordStep("Line 9: FLW(" + currentSymbol
                                                + ") ← FLW(" + currentSymbol
                                                + ") ∪ FOLLOW(" + lhs + ") ⇒ "
                                                + followSets.get(lhs),
                                        SetUtils.copySets(followSets), steps, 9);
                            }
                            SetUtils.recordStep("Line 10: end if",
                                    SetUtils.copySets(followSets), steps, 10);
                        }
                    }
                }
            }
        } while (changed);

        // Step 11: Stabilization complete
        SetUtils.recordStep("Line 11: FOLLOW sets stabilized, return FLW_{A}",
                SetUtils.copySets(followSets), steps, 11);

        // Persist results in grammar model
        grammar.setFollowSets(followSets);
        grammar.setFollowStepRecords(steps);
    }
}
