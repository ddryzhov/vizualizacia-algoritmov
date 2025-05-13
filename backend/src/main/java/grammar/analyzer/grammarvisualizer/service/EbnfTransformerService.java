package grammar.analyzer.grammarvisualizer.service;

/**
 * Service interface for converting EBNF grammar definitions into another format (e.g., BNF).
 * Implementations perform transformations on the grammar input string.
 */
public interface EbnfTransformerService {
    /**
     * Transforms the provided grammar input into the target format.
     * For example, converts EBNF syntax (with {}, [], ()) into equivalent BNF productions.
     *
     * @param grammarInput raw grammar definition string
     * @return transformed grammar string in the target format
     */
    String transform(String grammarInput);
}
