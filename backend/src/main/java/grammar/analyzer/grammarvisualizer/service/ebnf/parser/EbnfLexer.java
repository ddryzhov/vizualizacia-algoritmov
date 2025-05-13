package grammar.analyzer.grammarvisualizer.service.ebnf.parser;

import grammar.analyzer.grammarvisualizer.service.ebnf.model.token.Token;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.token.TokenType;
import java.util.ArrayList;
import java.util.List;

/**
 * Lexer for EBNF grammar strings. Splits input into tokens representing
 * parentheses, brackets, braces, pipes, literals, identifiers, and end-of-input.
 */
public class EbnfLexer {
    /**
     * Tokenizes the given EBNF input string into a list of Token objects.
     * Skips whitespace and handles single-quoted literals and identifiers.
     *
     * @param input raw EBNF grammar string
     * @return list of tokens ending with an END token
     */
    public static List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        int i = 0;
        while (i < input.length()) {
            char c = input.charAt(i);
            // Skip whitespace
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
                    i++; // Consume closing quote
                    tokens.add(new Token(TokenType.IDENTIFIER, "'" + literal + "'"));
                    continue;
                }
                default -> { // Unquoted identifier
                    int start = i;
                    // Consume until whitespace or special symbol
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
        // Append end-of-input marker
        tokens.add(new Token(TokenType.END, ""));
        return tokens;
    }
}
