package grammar.analyzer.grammarvisualizer.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import grammar.analyzer.grammarvisualizer.exception.GrammarSyntaxException;
import grammar.analyzer.grammarvisualizer.service.GrammarParserService;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class GrammarParserServiceImplTest {
    private final GrammarParserService parserService =
            new GrammarParserServiceImpl(new grammar.analyzer.grammarvisualizer
                    .service.ebnf.transformer.EbnfTransformerServiceImpl());

    @Test
    void testParseValidGrammar() {
        String grammarInput = "S -> 'a' | 'b'\nA -> 'c'";
        Map<String, List<String>> productionRules = parserService.parseGrammar(grammarInput);
        assertEquals(2, productionRules.size());
        assertTrue(productionRules.containsKey("S"));
        assertEquals(2, productionRules.get("S").size());
        assertTrue(productionRules.get("S").contains("'a'"));
        assertTrue(productionRules.get("S").contains("'b'"));
        assertTrue(productionRules.containsKey("A"));
    }

    @Test
    void testParseGrammarThrowsOnEmptyAlternative() {
        String grammarInput = "S -> 'a' | ";

        GrammarSyntaxException exception = assertThrows(
                GrammarSyntaxException.class,
                () -> parserService.parseGrammar(grammarInput)
        );

        assertTrue(exception.getMessage().contains("Empty alternative is not allowed. "
                + "Use 'epsilon' explicitly if needed"));
    }

    @Test
    void testParseInvalidGrammarMissingArrow() {
        String grammarInput = "S 'a'";
        Exception exception = assertThrows(GrammarSyntaxException.class,
                () -> parserService.parseGrammar(grammarInput));
        assertTrue(exception.getMessage().contains("must contain '->'"));
    }

    @Test
    void testParseInvalidLhs() {
        String grammarInput = "1S -> 'a'";
        Exception exception = assertThrows(GrammarSyntaxException.class,
                () -> parserService.parseGrammar(grammarInput));
        assertTrue(exception.getMessage().contains("left-hand side of "
                + "the rule must be a valid non-terminal"));
    }
}
