package grammar.analyzer.grammarvisualizer.service.ebnf.model.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents a repetition node in the EBNF AST.
 */
@Getter
@AllArgsConstructor
public class RepetitionNode extends EbnfNode {
    private EbnfNode node;
}
