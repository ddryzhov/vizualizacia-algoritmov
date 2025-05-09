package grammar.analyzer.grammarvisualizer.service;

import grammar.analyzer.grammarvisualizer.model.Grammar;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface for building the LL(1) table with detailed steps.
 */
public interface LL1Service {
    void buildLl1Table(
            Map<String, List<String>> productionRules,
            Map<String, Set<String>> predictSets,
            Grammar grammar
    );
}
