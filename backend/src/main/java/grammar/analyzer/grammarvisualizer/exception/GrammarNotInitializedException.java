package grammar.analyzer.grammarvisualizer.exception;

/**
 * Exception thrown when the grammar has not been initialized.
 */
public class GrammarNotInitializedException extends RuntimeException {
    public GrammarNotInitializedException(String message) {
        super(message);
    }
}
