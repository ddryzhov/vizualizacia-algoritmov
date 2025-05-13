package grammar.analyzer.grammarvisualizer.service.ebnf.model.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AST node representing a repeated EBNF element, denoted by braces {} in grammar.
 * Wraps an EBNF node that can appear zero or more times.
 */
@Getter
@AllArgsConstructor
public class RepetitionNode extends EbnfNode {
    /**
     * The EBNF node that may repeat in the grammar.
     */
    private EbnfNode node;
}
