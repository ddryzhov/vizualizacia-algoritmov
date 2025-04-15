package grammar.analyzer.grammarvisualizer.service.ebnf.model.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents a terminal symbol in the EBNF AST.
 */
@Getter
@AllArgsConstructor
public class TerminalNode extends EbnfNode {
    private String value;
}
