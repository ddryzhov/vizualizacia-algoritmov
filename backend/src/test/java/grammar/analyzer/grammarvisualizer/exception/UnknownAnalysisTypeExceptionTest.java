package grammar.analyzer.grammarvisualizer.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class UnknownAnalysisTypeExceptionTest {
    @Test
    void testExceptionMessage() {
        UnknownAnalysisTypeException ex = new UnknownAnalysisTypeException("INVALID_ANALYSIS");
        assertEquals("Unknown analysis type: INVALID_ANALYSIS", ex.getMessage());
    }
}
