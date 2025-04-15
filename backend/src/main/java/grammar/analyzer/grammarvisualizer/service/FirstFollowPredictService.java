package grammar.analyzer.grammarvisualizer.service;

import grammar.analyzer.grammarvisualizer.model.Grammar;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service interface for computing FIRST, FOLLOW, and PREDICT sets.
 */
public interface FirstFollowPredictService {
    void computeFirstSets(Map<String, List<String>> productionRules,
                          Set<String> nonTerminals, Grammar grammar);

    void computeFollowSets(Map<String, List<String>> productionRules,
                           Map<String, Set<String>> firstSets, Set<String> nonTerminals,
                           String startSymbol, Grammar grammar);

    void computePredictSets(Map<String, List<String>> productionRules,
                            Map<String, Set<String>> firstSets, Map<String, Set<String>> followSets,
                            Grammar grammar);
}
