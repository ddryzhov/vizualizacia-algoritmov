package grammar.analyzer.grammarvisualizer.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import grammar.analyzer.grammarvisualizer.model.Grammar;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class Ll1ServiceImplTest {
    @Test
    void testBuildLL1TableWithSteps() {
        Map<String, List<String>> productionRules = new LinkedHashMap<>();
        productionRules.put("S", Collections.singletonList("'a'"));
        Map<String, Set<String>> predictSets = new LinkedHashMap<>();
        predictSets.put("S -> 'a'", new LinkedHashSet<>(Collections.singletonList("'a'")));
        Grammar grammar = new Grammar();
        Ll1ServiceImpl service = new Ll1ServiceImpl();
        grammar.setProductionRuleNumbers(Map.of("S -> 'a'", 1));
        service.buildLl1Table(productionRules, predictSets, grammar);
        assertNotNull(grammar.getLl1Table());
        assertTrue(grammar.getLl1Table().containsKey("S"));
    }
}
