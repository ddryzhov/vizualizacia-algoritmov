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

/**
 * Component responsible for computing FIRST sets for a given grammar.
 * Records intermediate steps for visualization via StepRecord objects.
 */
@Component
public class FirstSetCalculator {
    /**
     * Computes and stores FIRST sets in the provided Grammar model.
     * Records initialization, each production evaluation, and stabilization steps.
     *
     * @param productionRules map of non-terminals to their production alternatives
     * @param nonTerminals    set of all non-terminal symbols in the grammar
     * @param grammar         Grammar model to populate with FIRST sets and step records
     */
    public void computeFirstSets(
            Map<String, List<String>> productionRules,
            Set<String> nonTerminals,
            Grammar grammar
    ) {
        // Initialize FIRST sets for all nonterminals
        Map<String, Set<String>> firstSets = SetUtils.initializeEmptySets(nonTerminals);
        List<StepRecord> steps = new ArrayList<>();

        // Record initial empty FIRST sets state
        SetUtils.recordStep("Initialize FIRST sets = ∅", SetUtils.copySets(firstSets), steps, 0);

        boolean changed;
        do {
            changed = false;
            // Process every production for each non-terminal
            for (String nonTerminal : productionRules.keySet()) {
                for (String production : productionRules.get(nonTerminal)) {
                    // Snapshot before computing FIRST
                    Map<String, Set<String>> before = SetUtils.copySets(firstSets);
                    computeFirstForProduction(nonTerminal, production, productionRules,
                            firstSets, nonTerminals, steps);
                    // If FIRST sets changed, mark for another iteration
                    if (!SetUtils.equalsSets(before, firstSets)) {
                        changed = true;
                    }
                }
            }
        } while (changed);

        // Record final stabilized state
        SetUtils.recordStep("FIRST sets stabilized", SetUtils.copySets(firstSets), steps, 15);

        // Save computed sets and steps into grammar object
        grammar.setFirstSets(firstSets);
        grammar.setFirstStepRecords(steps);
    }

    /**
     * Handles FIRST set computation for a single production rule.
     * Implements the standard algorithm with null (ε) propagation.
     * Records each decision and addition into step records.
     *
     * @param nonTerminal    left-hand nonterminal symbol
     * @param production     right-hand side production string (symbols separated by whitespace)
     * @param productionRules all grammar production rules for lookahead in nonterminals
     * @param firstSets      modifiable map of current FIRST sets
     * @param nonTerminals   set of nonterminal symbols for lookup
     * @param steps          list collecting StepRecord entries for visualization
     */
    private void computeFirstForProduction(
            String nonTerminal,
            String production,
            Map<String, List<String>> productionRules,
            Map<String, Set<String>> firstSets,
            Set<String> nonTerminals,
            List<StepRecord> steps
    ) {
        // Step 1: ε-production adds ε to FIRST(nonTerminal)
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

        // Step 2: Production starts with terminal => add terminal
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

        // Step 3: Production starts with non-terminal => proceed
        SetUtils.recordStep(
                "Step 3: Production starts with nonterminal '" + symbols[0]
                        + "'. Proceeding with nonterminal processing.",
                SetUtils.copySets(firstSets),
                steps,
                3
        );

        // Step 4: Initialize temporary set to accumulate FIRST outcomes
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
            // Iterate alternatives for recursive FIRST
            for (String gamma : productionsForA) {
                String[] gammaSymbols = gamma.split("\\s+");
                if (gammaSymbols.length > 0 && !gammaSymbols[0].equals(firstSymbol)) {
                    // Step 7: Add FIRST(gamma) to temporary set
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
                    // Step 6: Skip left-recursive rule
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

        // Step 10: If ε in temp set, process the remainder β
        if (fstAtemp.contains("ε")) {
            SetUtils.recordStep(
                    "Step 10: ε is in FSTA; process β from the production.",
                    SetUtils.copySets(firstSets),
                    steps,
                    10
            );

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

        // Step 13: Merge temp set into FIRST(nonTerminal)
        boolean updated = firstSets.get(nonTerminal).addAll(fstAtemp);
        if (updated) {
            SetUtils.recordStep(
                    "Step 13: Update FIRST(" + nonTerminal + ") = " + firstSets.get(nonTerminal),
                    SetUtils.copySets(firstSets),
                    steps,
                    13
            );
        }

        // Step 14: End of production processing
        SetUtils.recordStep(
                "Step 14: End processing production for " + nonTerminal,
                SetUtils.copySets(firstSets),
                steps,
                14
        );
    }

    /**
     * Recursively computes FIRST set for any symbol sequence (γ or β).
     * Adds terminals and ε where appropriate following standard algorithm.
     *
     * @param production   sequence of symbols to analyze
     * @param firstSets    existing FIRST sets for non-terminals
     * @param nonTerminals set of non-terminal symbols
     * @param result       set to accumulate FIRST(production)
     */
    public void computeFirstOfGamma(
            String production,
            Map<String, Set<String>> firstSets,
            Set<String> nonTerminals,
            Set<String> result
    ) {
        // Base: ε-production
        if ("epsilon".equals(production)) {
            result.add("ε");
            return;
        }

        String[] symbols = production.split("\\s+");
        // Terminal at start
        if (!nonTerminals.contains(symbols[0])) {
            result.add(symbols[0]);
            return;
        }

        // Process each symbol, stopping when ε is absent
        for (int i = 0; i < symbols.length; i++) {
            String symbol = symbols[i];
            Set<String> firstSymbol = GrammarUtils.computeFirstOfSymbol(symbol, firstSets);
            result.addAll(firstSymbol);
            if (!firstSymbol.contains("ε")) {
                break; // stop if no ε
            } else {
                result.remove("ε");
                if (i == symbols.length - 1) {
                    result.add("ε");
                }
            }
        }
    }
}
