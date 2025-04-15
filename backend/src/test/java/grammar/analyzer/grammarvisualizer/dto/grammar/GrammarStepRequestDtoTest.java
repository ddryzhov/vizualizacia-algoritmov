package grammar.analyzer.grammarvisualizer.dto.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GrammarStepRequestDtoTest {
    @Test
    void testGettersAndSetters() {
        GrammarStepRequestDto dto = new GrammarStepRequestDto();
        dto.setAnalysisType("FIRST");
        dto.setStepIndex(3);
        dto.setGrammar("S -> 'b'");

        assertEquals("FIRST", dto.getAnalysisType());
        assertEquals(3, dto.getStepIndex());
        assertEquals("S -> 'b'", dto.getGrammar());
    }
}
