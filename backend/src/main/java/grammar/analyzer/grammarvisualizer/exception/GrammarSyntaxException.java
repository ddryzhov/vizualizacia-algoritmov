package grammar.analyzer.grammarvisualizer.exception;

/**
 * Exception for handling grammar syntax errors.
 */
public class GrammarSyntaxException extends RuntimeException {
    public GrammarSyntaxException(String message) {
        super(message);
    }
}
