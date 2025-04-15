package grammar.analyzer.grammarvisualizer.service.ebnf.model.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents an optional element in the EBNF AST.
 */
@Getter
@AllArgsConstructor
public class OptionalNode extends EbnfNode {
    private EbnfNode node;
}
