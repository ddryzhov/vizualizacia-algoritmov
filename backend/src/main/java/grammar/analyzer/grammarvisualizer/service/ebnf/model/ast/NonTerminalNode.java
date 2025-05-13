package grammar.analyzer.grammarvisualizer.service.ebnf.model.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AST node representing a non-terminal symbol in EBNF grammar.
 * The `name` corresponds to the identifier of the non-terminal.
 */
@Getter
@AllArgsConstructor
public class NonTerminalNode extends EbnfNode {
    /**
     * Identifier of the non-terminal symbol.
     */
    private String name;
}
