package grammar.analyzer.grammarvisualizer.service.ebnf.transformer;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents an EBNF production rule.
 * Consists of a left-hand side (LHS) and a right-hand side (RHS).
 */
@Getter
@AllArgsConstructor
public class EbnfProduction {
    private String lhs;
    private String rhs;
}
