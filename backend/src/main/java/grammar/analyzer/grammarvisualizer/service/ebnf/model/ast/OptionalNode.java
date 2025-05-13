package grammar.analyzer.grammarvisualizer.service.ebnf.model.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AST node representing an optional EBNF element, denoted by square brackets [] in grammar.
 * Wraps another EBNF node that may or may not appear.
 */
@Getter
@AllArgsConstructor
public class OptionalNode extends EbnfNode {
    /**
     * The EBNF node that is optional in the grammar sequence.
     */
    private EbnfNode node;
}
