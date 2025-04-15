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
     * Computes the FIRST sets for the given grammar.
     *
     * @param productionRules the production rules of the grammar
     * @param nonTerminals    the set of nonterminal symbols
     * @param grammar         the grammar object to store the results
     */
    public void computeFirstSets(
            Map<String, List<String>> productionRules,
            Set<String> nonTerminals,
            Grammar grammar
    ) {
        // Initialize FIRST sets for all nonterminals as empty sets.
        Map<String, Set<String>> firstSets = SetUtils.initializeEmptySets(nonTerminals);
        List<StepRecord> steps = new ArrayList<>();

        // Initial state: record initialization of FIRST sets.
        SetUtils.recordStep("Initialize FIRST sets = ∅", SetUtils.copySets(firstSets), steps, 0);

        boolean changed;
        do {
            changed = false;
            // Iterate over every production rule.
            for (String nonTerminal : productionRules.keySet()) {
                for (String production : productionRules.get(nonTerminal)) {
                    // Make a copy of FIRST sets before processing.
                    Map<String, Set<String>> before = SetUtils.copySets(firstSets);
                    // Compute FIRST for the production.
                    computeFirstForProduction(nonTerminal, production, productionRules,
                            firstSets, nonTerminals, steps);
                    // Mark change if FIRST sets were updated.
                    if (!SetUtils.equalsSets(before, firstSets)) {
                        changed = true;
                    }
                }
            }
        } while (changed);

        // Record final state.
        SetUtils.recordStep("FIRST sets stabilized", SetUtils.copySets(firstSets), steps, 15);

        // Save results in the grammar object.
        grammar.setFirstSets(firstSets);
        grammar.setFirstStepRecords(steps);
    }

    /**
     * Computes the FIRST set for a specific production.
     *
     * @param nonTerminal    the nonterminal (LHS of production)
     * @param production     the production string (α)
     * @param productionRules the map of all production rules (needed for recursive processing)
     * @param firstSets      the current FIRST sets map
     * @param nonTerminals   the set of nonterminals
     * @param steps          list for recording visualization steps
     */
    private void computeFirstForProduction(
            String nonTerminal,
            String production,
            Map<String, List<String>> productionRules,
            Map<String, Set<String>> firstSets,
            Set<String> nonTerminals,
            List<StepRecord> steps
    ) {
        // --- Step 1 ---
        // if α = ε, then FIRST(α) = {ε}
        if ("epsilon".equals(production)) {
            firstSets.get(nonTerminal).add("ε");
            SetUtils.recordStep(
                    "Step 1: Production is ε, so FIRST(" + nonTerminal + ") becomes {ε}",
                    SetUtils.copySets(firstSets),
                    steps,
                    1
            );
            return;
        }

        // Split the production into symbols.
        String[] symbols = production.split("\\s+");

        // --- Step 2 ---
        // if α = aβ, with a terminal 'a' then FIRST(α) = {a}
        if (!nonTerminals.contains(symbols[0])) {
            firstSets.get(nonTerminal).add(symbols[0]);
            SetUtils.recordStep(
                    "Step 2: Production starts with terminal '" + symbols[0]
                            + "', so FIRST(" + nonTerminal + ") becomes {" + symbols[0] + "}",
                    SetUtils.copySets(firstSets),
                    steps,
                    2
            );
            return;
        }

        // --- Step 3 ---
        // if α = Aβ with A ∈ N, then process further.
        SetUtils.recordStep(
                "Step 3: Production starts with nonterminal '" + symbols[0]
                        + "'. Proceeding with nonterminal processing.",
                SetUtils.copySets(firstSets),
                steps,
                3
        );

        // --- Step 4 ---
        // Initialize temporary set FSTA = ∅.
        Set<String> fstAtemp = new LinkedHashSet<>();
        SetUtils.recordStep(
                "Step 4: Initialize temporary set FSTA = ∅",
                SetUtils.copySets(firstSets),
                steps,
                4
        );

        String firstSymbol = symbols[0];
        List<String> productionsForA = productionRules.get(firstSymbol);
        if (productionsForA != null) {
            // --- Step 5 ---
            // for each production firstSymbol → γ in P do:
            for (String gamma : productionsForA) {
                // --- Step 6 ---
                // if firstSymbol is not a prefix of γ then
                String[] gammaSymbols = gamma.split("\\s+");
                if (gammaSymbols.length > 0 && !gammaSymbols[0].equals(firstSymbol)) {
                    // --- Step 7 ---
                    // FSTA = FSTA ∪ FIRST(γ)
                    Set<String> firstGamma = new LinkedHashSet<>();
                    computeFirstOfGamma(gamma, firstSets, nonTerminals, firstGamma);
                    fstAtemp.addAll(firstGamma);
                    SetUtils.recordStep(
                            "Step 7: For production " + firstSymbol + " → " + gamma
                                    + ", add FIRST(" + gamma + ") = " + firstGamma + " to FSTA",
                            SetUtils.copySets(firstSets),
                            steps,
                            7
                    );
                } else {
                    // --- Step 6 (else) ---
                    // Skip left-recursive production.
                    SetUtils.recordStep(
                            "Step 6: Skipping production " + firstSymbol + " → " + gamma
                                    + " as it is left-recursive (starts with itself).",
                            SetUtils.copySets(firstSets),
                            steps,
                            6
                    );
                }
            }
        }

        // --- Step 10 ---
        // if ε ∈ FSTA then:
        if (fstAtemp.contains("ε")) {
            SetUtils.recordStep(
                    "Step 10: ε is in FSTA; process β from the production.",
                    SetUtils.copySets(firstSets),
                    steps,
                    10
            );
            // --- Step 11 ---
            // Remove ε and add FIRST(β)
            fstAtemp.remove("ε");
            String beta = "";
            if (symbols.length > 1) {
                beta = String.join(" ", java.util.Arrays.copyOfRange(symbols, 1, symbols.length));
            }
            Set<String> firstBeta = new LinkedHashSet<>();
            if (!beta.isEmpty()) {
                computeFirstOfGamma(beta, firstSets, nonTerminals, firstBeta);
            }
            fstAtemp.addAll(firstBeta);
            SetUtils.recordStep(
                    "Step 11: Updated FSTA after processing β: " + fstAtemp,
                    SetUtils.copySets(firstSets),
                    steps,
                    11
            );
        }

        // --- Step 13 ---
        // Update FIRST(nonTerminal) = FIRST(nonTerminal) ∪ FSTA.
        boolean updated = firstSets.get(nonTerminal).addAll(fstAtemp);
        if (updated) {
            SetUtils.recordStep(
                    "Step 13: Update FIRST(" + nonTerminal + ") = " + firstSets.get(nonTerminal),
                    SetUtils.copySets(firstSets),
                    steps,
                    13
            );
        }

        // --- Step 14 ---
        // End processing for this production.
        SetUtils.recordStep(
                "Step 14: End processing production for " + nonTerminal,
                SetUtils.copySets(firstSets),
                steps,
                14
        );
    }

    /**
     * Helper method that computes the FIRST set for an arbitrary production string (γ or β).
     *
     * @param production      the production string for which to compute FIRST
     * @param firstSets       current FIRST sets map
     * @param nonTerminals    the set of nonterminal symbols
     * @param result          the set in which to accumulate FIRST(production)
     */
    public void computeFirstOfGamma(
            String production,
            Map<String, Set<String>> firstSets,
            Set<String> nonTerminals,
            Set<String> result
    ) {
        // Base case: if production equals "epsilon", add ε.
        if ("epsilon".equals(production)) {
            result.add("ε");
            return;
        }

        String[] symbols = production.split("\\s+");
        // If production starts with a terminal, add it.
        if (!nonTerminals.contains(symbols[0])) {
            result.add(symbols[0]);
            return;
        }

        // Otherwise, process symbols sequentially.
        for (int i = 0; i < symbols.length; i++) {
            String symbol = symbols[i];
            Set<String> firstSymbol = GrammarUtils.computeFirstOfSymbol(symbol, firstSets);
            result.addAll(firstSymbol);
            if (!firstSymbol.contains("ε")) {
                break;
            } else {
                result.remove("ε");
                if (i == symbols.length - 1) {
                    result.add("ε");
                }
            }
        }
    }
}
