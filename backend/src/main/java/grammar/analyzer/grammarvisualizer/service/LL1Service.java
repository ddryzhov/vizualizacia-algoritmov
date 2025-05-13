package grammar.analyzer.grammarvisualizer.service;

import grammar.analyzer.grammarvisualizer.model.Grammar;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service interface for constructing the LL(1) parsing table and
 * determining grammar compliance with LL(1) conditions.
 */
public interface LL1Service {
    /**
     * Builds the LL(1) parse table based on the provided production rules and PREDICT sets.
     * Updates the given Grammar model with the table and a flag indicating whether the grammar is LL(1).
     *
     * @param productionRules map of non-terminal symbols to their production alternatives
     * @param predictSets     map associating each production rule (keyed by "A -> α") with its PREDICT set
     * @param grammar         Grammar model to populate with the LL(1) table and compliance flag
     */
    void buildLl1Table(
            Map<String, List<String>> productionRules,
            Map<String, Set<String>> predictSets,
            Grammar grammar
    );
}
