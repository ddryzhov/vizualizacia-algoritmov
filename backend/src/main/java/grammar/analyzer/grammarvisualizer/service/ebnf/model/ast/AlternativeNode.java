package grammar.analyzer.grammarvisualizer.service.ebnf.model.ast;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents an alternative in the EBNF abstract syntax tree.
 */
@Getter
@AllArgsConstructor
public class AlternativeNode extends EbnfNode {
    // List of alternative nodes.
    private List<EbnfNode> alternatives;
}
