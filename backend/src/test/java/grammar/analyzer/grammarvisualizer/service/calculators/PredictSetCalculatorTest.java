package grammar.analyzer.grammarvisualizer.service.calculators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import grammar.analyzer.grammarvisualizer.model.Grammar;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PredictSetCalculatorTest {
    private PredictSetCalculator calculator;
    private Grammar grammar;
    private Map<String, List<String>> productionRules;
    private Map<String, Set<String>> firstSets;
    private Map<String, Set<String>> followSets;

    @BeforeEach
    void setUp() {
        calculator = new PredictSetCalculator();
        grammar = new Grammar();
        productionRules = new LinkedHashMap<>();
        productionRules.put("S", Collections.singletonList("'a'"));
        firstSets = new LinkedHashMap<>();
        firstSets.put("S", new LinkedHashSet<>(Collections.singletonList("'a'")));
        followSets = new LinkedHashMap<>();
        followSets.put("S", new LinkedHashSet<>(Collections.singletonList("$")));
    }

    @Test
    void testComputePredictSetsWithoutEpsilon() {
        calculator.computePredictSets(productionRules, firstSets, followSets, grammar);
        Map<String, Set<String>> predictSets = grammar.getPredictSets();
        assertNotNull(predictSets);
        String key = "S -> 'a'";
        assertTrue(predictSets.containsKey(key));
        assertEquals(Collections.singleton("'a'"), predictSets.get(key));
    }

    @Test
    void testComputePredictSetsWithEpsilon() {
        productionRules.put("S", Collections.singletonList("epsilon"));
        firstSets.put("S", new LinkedHashSet<>(Collections.singletonList("ε")));
        calculator.computePredictSets(productionRules, firstSets, followSets, grammar);
        Map<String, Set<String>> predictSets = grammar.getPredictSets();
        String key = "S -> epsilon";
        assertTrue(predictSets.containsKey(key));
        assertEquals(Collections.singleton("$"), predictSets.get(key));
    }
}
