package grammar.analyzer.grammarvisualizer.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class LL1TableUtilsTest {
    @Test
    void testInitializeLl1Table() {
        Map<String, List<String>> productionRules = new LinkedHashMap<>();
        productionRules.put("S", List.of("'a'", "'b'"));
        Map<String, Map<String, String>> table = LL1TableUtils.initializeLl1Table(productionRules);
        assertTrue(table.containsKey("S"));
        Map<String, String> row = table.get("S");
        assertTrue(row.containsKey("'a'"));
        assertTrue(row.containsKey("'b'"));
        assertTrue(row.containsKey("$"));
        row.values().forEach(cell -> assertEquals("", cell));
    }

    @Test
    void testExtractTerminals() {
        Map<String, List<String>> productionRules = new LinkedHashMap<>();
        productionRules.put("S", List.of("'a' 'b'", "epsilon"));
        Set<String> terminals = LL1TableUtils.extractTerminals(productionRules);
        assertTrue(terminals.contains("'a'"));
        assertTrue(terminals.contains("'b'"));
        assertFalse(terminals.contains("epsilon"));
    }
}
