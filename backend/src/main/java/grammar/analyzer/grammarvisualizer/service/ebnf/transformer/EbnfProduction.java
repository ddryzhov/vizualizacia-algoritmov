package grammar.analyzer.grammarvisualizer.service.ebnf.transformer;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents a single BNF production derived from an EBNF node.
 * Contains left-hand side non-terminal and right-hand side definition.
 */
@Getter
@AllArgsConstructor
public class EbnfProduction {
    private String lhs;
    private String rhs;
}
