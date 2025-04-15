package grammar.analyzer.grammarvisualizer.service.ebnf.parser;

import grammar.analyzer.grammarvisualizer.service.ebnf.model.token.Token;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.token.TokenType;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple lexer that tokenizes an EBNF grammar input string.
 */
public class EbnfLexer {
    public static List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        int i = 0;
        while (i < input.length()) {
            char c = input.charAt(i);
            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }
            switch (c) {
                case '(' -> tokens.add(new Token(TokenType.LPAREN, "("));
                case ')' -> tokens.add(new Token(TokenType.RPAREN, ")"));
                case '[' -> tokens.add(new Token(TokenType.LBRACKET, "["));
                case ']' -> tokens.add(new Token(TokenType.RBRACKET, "]"));
                case '{' -> tokens.add(new Token(TokenType.LBRACE, "{"));
                case '}' -> tokens.add(new Token(TokenType.RBRACE, "}"));
                case '|' -> tokens.add(new Token(TokenType.PIPE, "|"));
                case '\'' -> {
                    StringBuilder literal = new StringBuilder();
                    i++;
                    while (i < input.length() && input.charAt(i) != '\'') {
                        literal.append(input.charAt(i++));
                    }
                    i++;
                    tokens.add(new Token(TokenType.IDENTIFIER, "'" + literal + "'"));
                    continue;
                }
                default -> {
                    int start = i;
                    while (i < input.length() && !Character.isWhitespace(input.charAt(i))
                            && "()[]{}|".indexOf(input.charAt(i)) == -1) {
                        i++;
                    }
                    tokens.add(new Token(TokenType.IDENTIFIER, input.substring(start, i)));
                    continue;
                }
            }
            i++;
        }
        tokens.add(new Token(TokenType.END, ""));
        return tokens;
    }
}
