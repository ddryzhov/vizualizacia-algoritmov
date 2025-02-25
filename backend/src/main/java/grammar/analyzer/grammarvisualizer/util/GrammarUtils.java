package grammar.analyzer.grammarvisualizer.util;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class GrammarUtils {
    /**
     * Compute the FIRST set for a single symbol.
     *
     * @param symbol       The symbol to compute FIRST for.
     * @param firstSets    The map of existing FIRST sets.
     * @param nonTerminals The set of non-terminal symbols.
     * @return The FIRST set for the given symbol.
     */
    public static Set<String> computeFirstOfSymbol(
            String symbol,
            Map<String, Set<String>> firstSets,
            Set<String> nonTerminals
    ) {
        if (!nonTerminals.contains(symbol) && !"epsilon".equals(symbol)) {
            return Set.of(symbol);
        } else if ("epsilon".equals(symbol)) {
            return Set.of("ε");
        } else {
            return firstSets.getOrDefault(symbol, Set.of());
        }
    }

    /**
     * Compute the FIRST set for a sequence of symbols (α).
     *
     * @param alpha        The sequence of symbols.
     * @param firstSets    The map of existing FIRST sets.
     * @param nonTerminals The set of non-terminal symbols.
     * @return The FIRST set for the given sequence.
     */
    public static Set<String> computeFirstOfAlpha(
            String[] alpha,
            Map<String, Set<String>> firstSets,
            Set<String> nonTerminals
    ) {
        if (alpha.length == 0) {
            return Set.of("ε");
        }
        Set<String> result = new LinkedHashSet<>();
        for (int i = 0; i < alpha.length; i++) {
            String cur = alpha[i];
            Set<String> firstSet = computeFirstOfSymbol(cur, firstSets, nonTerminals);

            result.addAll(firstSet);

            // Stop if epsilon is not in the current symbol's FIRST set
            if (!firstSet.contains("ε")) {
                break;
            }

            // If this is the last symbol and it has epsilon, retain epsilon
            if (i == alpha.length - 1 && firstSet.contains("ε")) {
                result.add("ε");
            } else {
                result.remove("ε");
            }
        }
        return result;
    }
}
