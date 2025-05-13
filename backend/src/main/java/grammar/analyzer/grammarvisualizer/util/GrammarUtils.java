package grammar.analyzer.grammarvisualizer.util;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Utility class providing methods to compute FIRST sets for symbols and symbol sequences.
 */
public class GrammarUtils {
    /**
     * Computes the FIRST set for a single symbol.
     *
     * @param symbol       the symbol for which to compute FIRST
     * @param firstSets    the map of existing FIRST sets
     * @return the FIRST set for the given symbol
     */
    public static Set<String> computeFirstOfSymbol(
            String symbol,
            Map<String, Set<String>> firstSets
    ) {
        if (symbol.startsWith("'") && symbol.endsWith("'")) {
            return Set.of(symbol);
        } else if ("epsilon".equals(symbol)) {
            return Set.of("ε");
        } else {
            return firstSets.getOrDefault(symbol, Set.of());
        }
    }

    /**
     * Computes the FIRST set for a sequence of grammar symbols (α).
     * Iterates symbols, adding their FIRST sets, stopping when ε is not present.
     * If all symbols derive ε, ε is included in the result.
     *
     * @param alpha     array of grammar symbols (terminals or non-terminals)
     * @param firstSets map of non-terminals to their FIRST sets
     * @return set of terminals representing FIRST(α)
     */
    public static Set<String> computeFirstOfAlpha(
            String[] alpha,
            Map<String, Set<String>> firstSets
    ) {
        if (alpha.length == 0) {
            return Set.of("ε");
        }
        Set<String> result = new LinkedHashSet<>();
        for (int i = 0; i < alpha.length; i++) {
            String cur = alpha[i];
            Set<String> firstSet = computeFirstOfSymbol(cur, firstSets);
            result.addAll(firstSet);
            if (!firstSet.contains("ε")) {
                break;
            }
            if (i == alpha.length - 1 && firstSet.contains("ε")) {
                result.add("ε");
            } else {
                result.remove("ε");
            }
        }
        return result;
    }
}
