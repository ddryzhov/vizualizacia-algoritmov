package grammar.analyzer.grammarvisualizer.dto.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GrammarRequestDtoTest {
    @Test
    void testGettersAndSetters() {
        GrammarRequestDto dto = new GrammarRequestDto();
        dto.setGrammar("S -> 'a'");
        assertEquals("S -> 'a'", dto.getGrammar());
    }
}
