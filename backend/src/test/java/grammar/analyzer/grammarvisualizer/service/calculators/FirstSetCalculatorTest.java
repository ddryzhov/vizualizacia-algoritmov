package grammar.analyzer.grammarvisualizer.service.calculators;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import grammar.analyzer.grammarvisualizer.model.Grammar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FirstSetCalculatorTest {
    private FirstSetCalculator calculator;
    private Grammar grammar;
    private Map<String, List<String>> productionRules;
    private Set<String> nonTerminals;

    @BeforeEach
    void setUp() {
        calculator = new FirstSetCalculator();
        grammar = new Grammar();
        productionRules = new LinkedHashMap<>();
        productionRules.put("S", Arrays.asList("'a'", "epsilon"));
        nonTerminals = new LinkedHashSet<>();
        nonTerminals.add("S");
    }

    @Test
    void testComputeFirstSets() {
        grammar.setFirstStepRecords(new ArrayList<>());
        calculator.computeFirstSets(productionRules, nonTerminals, grammar);
        Map<String, Set<String>> firstSets = grammar.getFirstSets();
        assertNotNull(firstSets);
        Set<String> firstS = firstSets.get("S");
        assertTrue(firstS.contains("'a'"));
        assertTrue(firstS.contains("ε"));
    }

    @Test
    void testComputeFirstOfGammaWithTerminal() {
        Map<String, Set<String>> firstSets = new HashMap<>();
        firstSets.put("S", new LinkedHashSet<>());
        Set<String> nonTerminals = new HashSet<>();
        nonTerminals.add("S");
        Set<String> result = new LinkedHashSet<>();
        calculator.computeFirstOfGamma("'b'", firstSets, nonTerminals, result);
        assertTrue(result.contains("'b'"));
    }

    @Test
    void testComputeFirstOfGammaWithEpsilon() {
        Map<String, Set<String>> firstSets = new HashMap<>();
        firstSets.put("S", new LinkedHashSet<>());
        Set<String> nonTerminals = new HashSet<>();
        nonTerminals.add("S");
        Set<String> result = new LinkedHashSet<>();
        calculator.computeFirstOfGamma("epsilon", firstSets, nonTerminals, result);
        assertTrue(result.contains("ε"));
    }
}
