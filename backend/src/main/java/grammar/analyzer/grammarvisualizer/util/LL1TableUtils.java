package grammar.analyzer.grammarvisualizer.util;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for initializing and extracting components of the LL(1) parse table.
 */
public class LL1TableUtils {
    /**
     * Creates an empty LL(1) table with rows for each non-terminal and columns for each terminal.
     * Initializes all table entries to empty strings and includes the end-of-input marker '$'.
     *
     * @param productionRules map of non-terminals to their production lists
     * @return nested map representing the LL(1) parsing table
     */
    public static Map<String, Map<String, String>> initializeLl1Table(
            Map<String, List<String>> productionRules
    ) {
        Map<String, Map<String, String>> table = new LinkedHashMap<>();
        Set<String> terminals = extractTerminals(productionRules);
        terminals.add("$");

        // Create a row for each non-terminal with empty entries for each terminal
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
     * Extracts terminal symbols from the grammar's production rules.
     * Recognizes quoted literals and excludes the epsilon keyword.
     *
     * @param productionRules map of non-terminals to their production lists
     * @return set of terminal symbols found in the grammar
     */
    public static Set<String> extractTerminals(Map<String, List<String>> productionRules) {
        Set<String> terminals = new LinkedHashSet<>();
        // Scan each production's right-hand side for quoted terminals
        for (List<String> rhsList : productionRules.values()) {
            for (String rhs : rhsList) {
                String[] symbols = rhs.split("\\s+");
                for (String s : symbols) {
                    // Add only literal terminals, ignore epsilon
                    if (!"epsilon".equals(s) && s.startsWith("'") && s.endsWith("'")) {
                        terminals.add(s);
                    }
                }
            }
        }
        return terminals;
    }
}
