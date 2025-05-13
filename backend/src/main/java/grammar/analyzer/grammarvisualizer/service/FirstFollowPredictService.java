package grammar.analyzer.grammarvisualizer.service;

import grammar.analyzer.grammarvisualizer.model.Grammar;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service interface defining methods to compute grammar analysis sets:
 * FIRST, FOLLOW, and PREDICT.
 */
public interface FirstFollowPredictService {
    /**
     * Computes the FIRST sets for each non-terminal in the grammar.
     * @param productionRules map of non-terminals to their production lists
     * @param nonTerminals    set of all non-terminal symbols
     * @param grammar         Grammar model to populate with FIRST sets
     */
    void computeFirstSets(Map<String, List<String>> productionRules,
                          Set<String> nonTerminals, Grammar grammar);

    /**
     * Computes the FOLLOW sets for each non-terminal based on FIRST sets and start symbol.
     * @param productionRules map of non-terminals to their production lists
     * @param firstSets       precomputed FIRST sets for lookahead
     * @param nonTerminals    set of all non-terminal symbols
     * @param startSymbol     the grammar's start symbol for end-marker initialization
     * @param grammar         Grammar model to populate with FOLLOW sets
     */
    void computeFollowSets(Map<String, List<String>> productionRules,
                           Map<String, Set<String>> firstSets, Set<String> nonTerminals,
                           String startSymbol, Grammar grammar);

    /**
     * Computes the PREDICT sets for each production, combining FIRST and FOLLOW when ε is present.
     * @param productionRules map of non-terminals to their production lists
     * @param firstSets       computed FIRST sets
     * @param followSets      computed FOLLOW sets
     * @param grammar         Grammar model to populate with PREDICT sets
     */
    void computePredictSets(Map<String, List<String>> productionRules,
                            Map<String, Set<String>> firstSets, Map<String, Set<String>> followSets,
                            Grammar grammar);
}
