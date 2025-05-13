package grammar.analyzer.grammarvisualizer.service.ebnf.model.ast;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AST node representing a sequence of EBNF elements.
 * The elements list defines the ordered nodes that must appear in sequence.
 */
@Getter
@AllArgsConstructor
public class SequenceNode extends EbnfNode {
    /**
     * Ordered list of EBNF nodes composing the sequence.
     */
    private List<EbnfNode> elements;
}
