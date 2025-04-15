package grammar.analyzer.grammarvisualizer.service.ebnf.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import grammar.analyzer.grammarvisualizer.service.ebnf.model.token.Token;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.token.TokenType;
import java.util.List;
import org.junit.jupiter.api.Test;

class EbnfLexerTest {
    @Test
    void testTokenizeSimple() {
        String input = "a ( 'b' )";
        List<Token> tokens = EbnfLexer.tokenize(input);
        assertEquals(5, tokens.size());
        assertEquals(TokenType.IDENTIFIER, tokens.get(0).getType());
        assertEquals("a", tokens.get(0).getText());
        assertEquals(TokenType.LPAREN, tokens.get(1).getType());
        assertEquals(TokenType.IDENTIFIER, tokens.get(2).getType());
        assertEquals("'b'", tokens.get(2).getText());
        assertEquals(TokenType.RPAREN, tokens.get(3).getType());
        assertEquals(TokenType.END, tokens.get(4).getType());
    }

    @Test
    void testTokenizeWithPipes() {
        String input = "A | B";
        List<Token> tokens = EbnfLexer.tokenize(input);
        assertEquals(4, tokens.size());
        assertEquals("A", tokens.get(0).getText());
        assertEquals(TokenType.PIPE, tokens.get(1).getType());
        assertEquals("B", tokens.get(2).getText());
    }
}
