package grammar.analyzer.grammarvisualizer.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class GrammarUtilsTest {
    @Test
    void testComputeFirstOfSymbolTerminal() {
        Map<String, Set<String>> firstSets = new HashMap<>();
        Set<String> result = GrammarUtils.computeFirstOfSymbol("'a'", firstSets);
        assertEquals(Set.of("'a'"), result);
    }

    @Test
    void testComputeFirstOfSymbolEpsilon() {
        Map<String, Set<String>> firstSets = new HashMap<>();
        Set<String> result = GrammarUtils.computeFirstOfSymbol("epsilon", firstSets);
        assertEquals(Set.of("ε"), result);
    }

    @Test
    void testComputeFirstOfSymbolNonTerminal() {
        Map<String, Set<String>> firstSets = new HashMap<>();
        firstSets.put("A", Set.of("'a'"));
        Set<String> result = GrammarUtils.computeFirstOfSymbol("A", firstSets);
        assertEquals(Set.of("'a'"), result);
    }

    @Test
    void testComputeFirstOfAlpha() {
        Map<String, Set<String>> firstSets = new HashMap<>();
        firstSets.put("A", Set.of("'a'", "ε"));
        String[] alpha = {"A", "'b'"};
        Set<String> result = GrammarUtils.computeFirstOfAlpha(alpha, firstSets);
        assertTrue(result.contains("'a'"));
        assertTrue(result.contains("'b'"));
        assertFalse(result.contains("ε"));
    }
}
