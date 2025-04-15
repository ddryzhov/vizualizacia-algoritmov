package grammar.analyzer.grammarvisualizer.service.ebnf.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import grammar.analyzer.grammarvisualizer.service.ebnf.model.ast.AlternativeNode;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.ast.EbnfNode;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.ast.NonTerminalNode;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.ast.OptionalNode;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.ast.RepetitionNode;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.ast.TerminalNode;
import org.junit.jupiter.api.Test;

class EbnfParserTest {
    @Test
    void testParseExpressionTerminal() {
        String input = "'a'";
        EbnfParser parser = new EbnfParser(input);
        EbnfNode node = parser.parseExpression();
        assertTrue(node instanceof TerminalNode);
        TerminalNode term = (TerminalNode) node;
        assertEquals("'a'", term.getValue());
    }

    @Test
    void testParseExpressionNonTerminal() {
        String input = "A";
        EbnfParser parser = new EbnfParser(input);
        EbnfNode node = parser.parseExpression();
        assertTrue(node instanceof NonTerminalNode);
        NonTerminalNode nonTerm = (NonTerminalNode) node;
        assertEquals("A", nonTerm.getName());
    }

    @Test
    void testParseExpressionWithAlternation() {
        String input = "A | B";
        EbnfParser parser = new EbnfParser(input);
        EbnfNode node = parser.parseExpression();
        assertTrue(node instanceof AlternativeNode);
        AlternativeNode alt = (AlternativeNode) node;
        assertEquals(2, alt.getAlternatives().size());
    }

    @Test
    void testParseExpressionWithGrouping() {
        String input = "(A)";
        EbnfParser parser = new EbnfParser(input);
        EbnfNode node = parser.parseExpression();
        assertTrue(node instanceof NonTerminalNode);
        NonTerminalNode nonTerm = (NonTerminalNode) node;
        assertEquals("A", nonTerm.getName());
    }

    @Test
    void testParseExpressionWithOptional() {
        String input = "[A]";
        EbnfParser parser = new EbnfParser(input);
        EbnfNode node = parser.parseExpression();
        assertTrue(node instanceof OptionalNode);
    }

    @Test
    void testParseExpressionWithRepetition() {
        String input = "{A}";
        EbnfParser parser = new EbnfParser(input);
        EbnfNode node = parser.parseExpression();
        assertTrue(node instanceof RepetitionNode);
    }

    @Test
    void testParseExpressionMissingClosing() {
        String input = "(A";
        EbnfParser parser = new EbnfParser(input);
        Exception exception = assertThrows(RuntimeException.class, parser::parseExpression);
        assertTrue(exception.getMessage().contains("Expected ')'"));
    }
}
