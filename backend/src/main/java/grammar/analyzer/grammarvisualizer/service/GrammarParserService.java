package grammar.analyzer.grammarvisualizer.service;

import java.util.List;
import java.util.Map;

public interface GrammarParserService {
    Map<String, List<String>> parseGrammar(String grammarInput);
}
