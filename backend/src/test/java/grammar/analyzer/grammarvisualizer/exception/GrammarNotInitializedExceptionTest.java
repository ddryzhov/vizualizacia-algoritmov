package grammar.analyzer.grammarvisualizer.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GrammarNotInitializedExceptionTest {
    @Test
    void testExceptionMessage() {
        GrammarNotInitializedException ex = new GrammarNotInitializedException("Not initialized");
        assertEquals("Not initialized", ex.getMessage());
    }
}
