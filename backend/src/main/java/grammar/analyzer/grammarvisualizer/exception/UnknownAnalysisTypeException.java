package grammar.analyzer.grammarvisualizer.exception;

/**
 * Exception for an unknown analysis type.
 */
public class UnknownAnalysisTypeException extends RuntimeException {
    public UnknownAnalysisTypeException(String analysisType) {
        super("Unknown analysis type: " + analysisType);
    }
}
