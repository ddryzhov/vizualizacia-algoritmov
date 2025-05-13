package grammar.analyzer.grammarvisualizer.service.ebnf.model.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AST node representing a terminal symbol in the EBNF grammar.
 * The `value` corresponds to the literal string expected in the input.
 */
@Getter
@AllArgsConstructor
public class TerminalNode extends EbnfNode {
    /**
     * The literal terminal string to match.
     */
    private String value;
}
