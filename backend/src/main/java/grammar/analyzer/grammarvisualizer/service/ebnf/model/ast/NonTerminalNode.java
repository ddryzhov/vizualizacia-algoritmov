package grammar.analyzer.grammarvisualizer.service.ebnf.model.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents a non-terminal symbol in the EBNF AST.
 */
@Getter
@AllArgsConstructor
public class NonTerminalNode extends EbnfNode {
    private String name;
}
