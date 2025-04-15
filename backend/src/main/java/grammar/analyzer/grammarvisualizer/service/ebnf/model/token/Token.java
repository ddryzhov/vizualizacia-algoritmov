package grammar.analyzer.grammarvisualizer.service.ebnf.model.token;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents a token with a specific type and text.
 */
@Getter
@AllArgsConstructor
public class Token {
    private TokenType type;
    private String text;
}
