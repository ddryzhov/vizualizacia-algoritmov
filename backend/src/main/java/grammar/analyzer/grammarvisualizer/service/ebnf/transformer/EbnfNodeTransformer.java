package grammar.analyzer.grammarvisualizer.service.ebnf.transformer;

import grammar.analyzer.grammarvisualizer.service.ebnf.model.ast.AlternativeNode;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.ast.EbnfNode;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.ast.NonTerminalNode;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.ast.OptionalNode;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.ast.RepetitionNode;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.ast.SequenceNode;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.ast.TerminalNode;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Transforms EBNF AST nodes into equivalent BNF productions.
 * Generates new non-terminals for alternative, optional, and repetition constructs.
 */
public class EbnfNodeTransformer {

    public interface NonTerminalGenerator {
        String generate(String prefix);
    }

    /**
     * Transforms an EBNF node into a BNF representation.
     *
     * @param node                  the EBNF node to transform
     * @param additionalProductions a list to store newly generated productions
     * @param generator             the generator for creating new non-terminal names
     * @return the BNF string representation of the node
     */
    public static String transformNode(EbnfNode node, List<EbnfProduction> additionalProductions,
                                       NonTerminalGenerator generator) {
        if (node instanceof TerminalNode term) {
            return term.getValue();
        } else if (node instanceof NonTerminalNode nonTerm) {
            return nonTerm.getName();
        } else if (node instanceof SequenceNode seq) {
            return seq.getElements().stream()
                    .map(child -> transformNode(child, additionalProductions, generator))
                    .collect(Collectors.joining(" "));
        } else if (node instanceof AlternativeNode alt) {
            String newNonTerminal = generator.generate("alt");
            String rhs = alt.getAlternatives().stream()
                    .map(child -> transformNode(child, additionalProductions, generator))
                    .collect(Collectors.joining(" | "));
            additionalProductions.add(new EbnfProduction(newNonTerminal, rhs));
            return newNonTerminal;
        } else if (node instanceof OptionalNode opt) {
            String newNonTerminal = generator.generate("opt");
            String inner = transformNode(opt.getNode(), additionalProductions, generator);
            additionalProductions.add(new EbnfProduction(newNonTerminal, inner + " | epsilon"));
            return newNonTerminal;
        } else if (node instanceof RepetitionNode rep) {
            String newNonTerminal = generator.generate("rep");
            String inner = transformNode(rep.getNode(), additionalProductions, generator);
            additionalProductions.add(new EbnfProduction(newNonTerminal,
                    inner + " " + newNonTerminal + " | epsilon"));
            return newNonTerminal;
        }
        return "";
    }
}
