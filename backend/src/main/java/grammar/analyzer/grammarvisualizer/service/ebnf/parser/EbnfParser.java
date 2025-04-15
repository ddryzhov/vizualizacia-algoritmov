package grammar.analyzer.grammarvisualizer.service.ebnf.parser;

import grammar.analyzer.grammarvisualizer.service.ebnf.model.ast.AlternativeNode;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.ast.EbnfNode;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.ast.NonTerminalNode;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.ast.OptionalNode;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.ast.RepetitionNode;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.ast.SequenceNode;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.ast.TerminalNode;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.token.Token;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.token.TokenType;
import java.util.ArrayList;
import java.util.List;

/**
 * A recursive descent parser for EBNF grammars.
 */
public class EbnfParser {

    private final List<Token> tokens;
    private int pos;

    public EbnfParser(String input) {
        this.tokens = EbnfLexer.tokenize(input);
        this.pos = 0;
    }

    private Token peek() {
        return pos < tokens.size() ? tokens.get(pos) : new Token(TokenType.END, "");
    }

    private Token consume() {
        return tokens.get(pos++);
    }

    private boolean match(TokenType type) {
        if (peek().getType() == type) {
            consume();
            return true;
        }
        return false;
    }

    /**
     * Parses an expression which may contain alternatives.
     *
     * @return the parsed EBNF node
     */
    public EbnfNode parseExpression() {
        EbnfNode left = parseTerm();
        if (peek().getType() == TokenType.PIPE) {
            AlternativeNode alt = new AlternativeNode(new ArrayList<>());
            alt.getAlternatives().add(left);
            while (match(TokenType.PIPE)) {
                alt.getAlternatives().add(parseTerm());
            }
            return alt;
        }
        return left;
    }

    /**
     * Parses a term which is a sequence of factors.
     *
     * @return the parsed EBNF node
     */
    private EbnfNode parseTerm() {
        SequenceNode seq = new SequenceNode(new ArrayList<>());
        while (!List.of(TokenType.RPAREN, TokenType.RBRACKET,
                        TokenType.RBRACE, TokenType.PIPE, TokenType.END)
                .contains(peek().getType())) {
            seq.getElements().add(parseFactor());
        }
        return seq.getElements().size() == 1 ? seq.getElements().get(0) : seq;
    }

    private EbnfNode parseFactor() {
        return parsePrimary();
    }

    /**
     * Parses a primary element: terminal, non-terminal, or grouped expression.
     *
     * @return the parsed EBNF node
     */
    private EbnfNode parsePrimary() {
        Token t = peek();
        if (t.getType() == TokenType.IDENTIFIER) {
            consume();
            return t.getText().startsWith("'") ? new TerminalNode(t.getText())
                    : new NonTerminalNode(t.getText());
        } else if (match(TokenType.LPAREN)) {
            EbnfNode node = parseExpression();
            if (!match(TokenType.RPAREN)) {
                throw new RuntimeException("Expected ')' token");
            }
            return node;
        } else if (match(TokenType.LBRACKET)) {
            EbnfNode node = parseExpression();
            if (!match(TokenType.RBRACKET)) {
                throw new RuntimeException("Expected ']' token");
            }
            return new OptionalNode(node);
        } else if (match(TokenType.LBRACE)) {
            EbnfNode node = parseExpression();
            if (!match(TokenType.RBRACE)) {
                throw new RuntimeException("Expected '}' token");
            }
            return new RepetitionNode(node);
        } else {
            throw new RuntimeException("Unexpected token: " + t);
        }
    }
}
