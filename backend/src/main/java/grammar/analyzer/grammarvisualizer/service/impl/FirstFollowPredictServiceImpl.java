package grammar.analyzer.grammarvisualizer.service.impl;

import grammar.analyzer.grammarvisualizer.model.Grammar;
import grammar.analyzer.grammarvisualizer.service.FirstFollowPredictService;
import grammar.analyzer.grammarvisualizer.service.calculators.FirstSetCalculator;
import grammar.analyzer.grammarvisualizer.service.calculators.FollowSetCalculator;
import grammar.analyzer.grammarvisualizer.service.calculators.PredictSetCalculator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service implementation that orchestrates the computation of FIRST, FOLLOW, and PREDICT sets.
 */
@Service
@RequiredArgsConstructor
public class FirstFollowPredictServiceImpl implements FirstFollowPredictService {
    private final FirstSetCalculator firstSetCalculator;
    private final FollowSetCalculator followSetCalculator;
    private final PredictSetCalculator predictSetCalculator;

    /**
     * Delegates computation of FIRST sets to FirstSetCalculator.
     *
     * @param productionRules grammar production rules
     * @param nonTerminals    set of non-terminal symbols
     * @param grammar         Grammar model to populate with FIRST sets
     */
    @Override
    public void computeFirstSets(
            Map<String, List<String>> productionRules,
            Set<String> nonTerminals,
            Grammar grammar
    ) {
        firstSetCalculator.computeFirstSets(productionRules, nonTerminals, grammar);
    }

    /**
     * Delegates computation of FOLLOW sets to FollowSetCalculator.
     *
     * @param productionRules grammar production rules
     * @param firstSets       precomputed FIRST sets for lookahead
     * @param nonTerminals    set of non-terminal symbols
     * @param startSymbol     start symbol to initialize FOLLOW(startSymbol)
     * @param grammar         Grammar model to populate with FOLLOW sets
     */
    @Override
    public void computeFollowSets(
            Map<String, List<String>> productionRules,
            Map<String, Set<String>> firstSets,
            Set<String> nonTerminals,
            String startSymbol,
            Grammar grammar
    ) {
        followSetCalculator.computeFollowSets(productionRules, firstSets,
                nonTerminals, startSymbol, grammar);
    }

    /**
     * Delegates computation of PREDICT sets to PredictSetCalculator.
     *
     * @param productionRules grammar production rules
     * @param firstSets       computed FIRST sets
     * @param followSets      computed FOLLOW sets
     * @param grammar         Grammar model to populate with PREDICT sets
     */
    @Override
    public void computePredictSets(
            Map<String, List<String>> productionRules,
            Map<String, Set<String>> firstSets,
            Map<String, Set<String>> followSets,
            Grammar grammar
    ) {
        predictSetCalculator.computePredictSets(productionRules, firstSets, followSets, grammar);
    }
}
