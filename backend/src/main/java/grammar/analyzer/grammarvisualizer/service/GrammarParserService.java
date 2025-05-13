package grammar.analyzer.grammarvisualizer.service;

import java.util.List;
import java.util.Map;

/**
 * Service interface for parsing raw grammar definitions into production rule maps.
 * Supports validation of syntax and handling of both BNF and transformed EBNF inputs.
 */
public interface GrammarParserService {
    /**
     * Parses the given grammar input string into a map of non-terminals to their production lists.
     * Throws an exception if syntax is invalid or undefined symbols are present.
     *
     * @param grammarInput multiline grammar definition string
     * @return ordered map where keys are non-terminals and values are lists of productions
     */
    Map<String, List<String>> parseGrammar(String grammarInput);
}
