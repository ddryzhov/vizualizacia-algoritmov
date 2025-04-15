package grammar.analyzer.grammarvisualizer.util;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LL1TableUtils {
    /**
     * Initialize an empty LL(1) table.
     *
     * @param productionRules the grammar's production rules
     * @return an empty LL(1) table
     */
    public static Map<String, Map<String, String>> initializeLl1Table(
            Map<String, List<String>> productionRules
    ) {
        Map<String, Map<String, String>> table = new LinkedHashMap<>();
        Set<String> terminals = extractTerminals(productionRules);
        terminals.add("$"); // Add end-of-input marker

        for (String nonTerminal : productionRules.keySet()) {
            Map<String, String> row = new LinkedHashMap<>();
            for (String terminal : terminals) {
                row.put(terminal, "");
            }
            table.put(nonTerminal, row);
        }
        return table;
    }

    /**
     * Extract terminal symbols from production rules.
     *
     * @param productionRules the grammar's production rules
     * @return a set of terminal symbols
     */
    public static Set<String> extractTerminals(Map<String, List<String>> productionRules) {
        Set<String> terminals = new LinkedHashSet<>();
        for (List<String> rhsList : productionRules.values()) {
            for (String rhs : rhsList) {
                String[] symbols = rhs.split("\\s+");
                for (String s : symbols) {
                    if (!"epsilon".equals(s) && s.startsWith("'") && s.endsWith("'")) {
                        terminals.add(s);
                    }
                }
            }
        }
        return terminals;
    }
}
