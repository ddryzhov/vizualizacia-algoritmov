package grammar.analyzer.grammarvisualizer.service.ebnf.model.ast;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents a sequence of EBNF nodes.
 */
@Getter
@AllArgsConstructor
public class SequenceNode extends EbnfNode {
    private List<EbnfNode> elements;
}
