package grammar.analyzer.grammarvisualizer.service;

import grammar.analyzer.grammarvisualizer.model.Grammar;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface LL1Service {
    void buildLL1TableWithSteps(
            Map<String, List<String>> productionRules,
            Map<String, Set<String>> predictSets,
            Grammar grammar
    );
}
