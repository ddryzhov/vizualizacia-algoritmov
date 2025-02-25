package grammar.analyzer.grammarvisualizer.exception;

public class UnknownAnalysisTypeException extends RuntimeException {
    public UnknownAnalysisTypeException(String analysisType) {
        super("Unknown analysis type: " + analysisType);
    }
}
