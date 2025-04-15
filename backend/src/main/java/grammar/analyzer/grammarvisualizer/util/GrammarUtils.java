package grammar.analyzer.grammarvisualizer.util;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

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
     * Computes the FIRST set for a sequence of symbols (α).
     *
     * @param alpha        the array of symbols representing the sequence
     * @param firstSets    the map of existing FIRST sets
     * @return the FIRST set for the sequence α
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
