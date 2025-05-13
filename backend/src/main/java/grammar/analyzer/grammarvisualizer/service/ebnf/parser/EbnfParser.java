package grammar.analyzer.grammarvisualizer.service.ebnf.parser;

import grammar.analyzer.grammarvisualizer.exception.GrammarSyntaxException;
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
 * Recursive-descent parser for EBNF grammar definitions.
 * Converts a token stream into an AST of EbnfNode objects.
 */
public class EbnfParser {
    private final List<Token> tokens;
    private int pos;

    /**
     * Initializes the parser with the input string by lexing into tokens.
     * @param input raw EBNF grammar text
     */
    public EbnfParser(String input) {
        this.tokens = EbnfLexer.tokenize(input);
        this.pos = 0;
    }

    /**
     * Peeks at the current token without consuming it.
     * @return current token or END token if at input end
     */
    private Token peek() {
        return pos < tokens.size() ? tokens.get(pos) : new Token(TokenType.END, "");
    }

    /**
     * Consumes and returns the current token, advancing the position.
     */
    private Token consume() {
        return tokens.get(pos++);
    }

    /**
     * If the current token matches the given type, consumes it and returns true.
     * @param type expected token type
     */
    private boolean match(TokenType type) {
        if (peek().getType() == type) {
            consume();
            return true;
        }
        return false;
    }

    /**
     * Parses an expression, handling alternatives separated by '|'.
     * @return AST node representing the expression or alternatives
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
     * Parses a term consisting of a sequence of factors until a delimiter token.
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

    /**
     * Parses a factor; currently delegates to primary.
     */
    private EbnfNode parseFactor() {
        return parsePrimary();
    }

    /**
     * Parses a primary EBNF construct: identifier, grouped expression,
     * optional, or repetition.
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
                throw new GrammarSyntaxException("Expected ')' token");
            }
            return node;
        } else if (match(TokenType.LBRACKET)) {
            EbnfNode node = parseExpression();
            if (!match(TokenType.RBRACKET)) {
                throw new GrammarSyntaxException("Expected ']' token");
            }
            return new OptionalNode(node);
        } else if (match(TokenType.LBRACE)) {
            EbnfNode node = parseExpression();
            if (!match(TokenType.RBRACE)) {
                throw new GrammarSyntaxException("Expected '}' token");
            }
            return new RepetitionNode(node);
        } else {
            throw new GrammarSyntaxException("Unexpected token: " + t);
        }
    }
}
