package grammar.analyzer.grammarvisualizer.service.ebnf.model.token;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents a lexical token extracted from EBNF input.
 * Each token has a type and its raw text value.
 */
@Getter
@AllArgsConstructor
public class Token {
    private TokenType type;
    private String text;
}
