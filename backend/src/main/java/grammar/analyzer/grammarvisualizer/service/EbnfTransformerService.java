package grammar.analyzer.grammarvisualizer.service;

/**
 * Interface for transforming EBNF grammar into another format (e.g., BNF).
 */
public interface EbnfTransformerService {
    String transform(String grammarInput);
}
