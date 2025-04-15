package grammar.analyzer.grammarvisualizer.service.calculators;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import grammar.analyzer.grammarvisualizer.model.Grammar;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FollowSetCalculatorTest {
    private FollowSetCalculator calculator;
    private Grammar grammar;
    private Map<String, List<String>> productionRules;
    private Map<String, Set<String>> firstSets;
    private Set<String> nonTerminals;
    private String startSymbol;

    @BeforeEach
    void setUp() {
        calculator = new FollowSetCalculator();
        grammar = new Grammar();
        productionRules = new LinkedHashMap<>();
        productionRules.put("S", Collections.singletonList("A"));
        productionRules.put("A", Collections.singletonList("'a'"));
        nonTerminals = new LinkedHashSet<>(Arrays.asList("S", "A"));
        startSymbol = "S";
        firstSets = new LinkedHashMap<>();
        firstSets.put("S", new LinkedHashSet<>(Collections.singletonList("'a'")));
        firstSets.put("A", new LinkedHashSet<>(Collections.singletonList("'a'")));
    }

    @Test
    void testComputeFollowSets() {
        calculator.computeFollowSets(productionRules, firstSets,
                nonTerminals, startSymbol, grammar);
        Map<String, Set<String>> followSets = grammar.getFollowSets();
        assertNotNull(followSets);
        assertTrue(followSets.get("S").contains("$"));
    }
}
