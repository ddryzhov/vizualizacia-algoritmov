package grammar.analyzer.grammarvisualizer.service.ebnf.model.ast;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AST node representing a set of alternative EBNF expressions.
 * Each alternative is a possible branch in the grammar definition.
 */
@Getter
@AllArgsConstructor
public class AlternativeNode extends EbnfNode {
    /**
     * A list of EBNF nodes, each representing one alternative branch.
     */
    private List<EbnfNode> alternatives;
}
