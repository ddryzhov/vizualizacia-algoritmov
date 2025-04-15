package grammar.analyzer.grammarvisualizer.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GrammarSyntaxExceptionTest {
    @Test
    void testExceptionMessage() {
        GrammarSyntaxException ex = new GrammarSyntaxException("Syntax error");
        assertEquals("Syntax error", ex.getMessage());
    }
}
