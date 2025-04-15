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
 * Service for computing FIRST, FOLLOW, and PREDICT sets.
 */
@Service
@RequiredArgsConstructor
public class FirstFollowPredictServiceImpl implements FirstFollowPredictService {
    private final FirstSetCalculator firstSetCalculator;
    private final FollowSetCalculator followSetCalculator;
    private final PredictSetCalculator predictSetCalculator;

    /**
     * Computes the FIRST sets for the grammar.
     *
     * @param productionRules the grammar production rules
     * @param nonTerminals    the set of non-terminal symbols
     * @param grammar         the grammar object to store the results
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
     * Computes the FOLLOW sets for the grammar.
     *
     * @param productionRules the grammar production rules
     * @param firstSets       the previously computed FIRST sets
     * @param nonTerminals    the set of non-terminal symbols
     * @param startSymbol     the start symbol of the grammar
     * @param grammar         the grammar object to store the results
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
     * Computes the PREDICT sets for the grammar.
     *
     * @param productionRules the grammar production rules
     * @param firstSets       the computed FIRST sets
     * @param followSets      the computed FOLLOW sets
     * @param grammar         the grammar object to store the results
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
