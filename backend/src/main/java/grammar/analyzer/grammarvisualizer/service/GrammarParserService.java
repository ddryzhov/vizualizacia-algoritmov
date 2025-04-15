package grammar.analyzer.grammarvisualizer.service;

import java.util.List;
import java.util.Map;

/**
 * Parses a grammar input string into production rules.
 */
public interface GrammarParserService {
    Map<String, List<String>> parseGrammar(String grammarInput);
}
