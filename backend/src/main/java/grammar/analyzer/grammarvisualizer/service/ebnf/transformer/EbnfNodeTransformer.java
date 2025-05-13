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
 * Converts EBNF AST nodes into BNF productions.
 * Generates helper non-terminals for alternatives, optionals, and repetitions.
 */
public class EbnfNodeTransformer {
    /**
     * Functional interface for generating unique non-terminal names.
     */
    public interface NonTerminalGenerator {
        String generate(String prefix);
    }

    /**
     * Transforms an EBNF AST node into its BNF string representation.
     * Appends any newly created productions to the provided list.
     *
     * @param node                  the EBNF AST node to transform
     * @param additionalProductions list collecting generated BNF productions
     * @param generator             non-terminal name generator
     * @return BNF fragment or non-terminal name representing the node
     */
    public static String transformNode(EbnfNode node, List<EbnfProduction> additionalProductions,
                                       NonTerminalGenerator generator) {
        if (node instanceof TerminalNode term) {
            // Return literal terminal value
            return term.getValue();
        } else if (node instanceof NonTerminalNode nonTerm) {
            // Return existing non-terminal name
            return nonTerm.getName();
        } else if (node instanceof SequenceNode seq) {
            // Concatenate child nodes with space separator
            return seq.getElements().stream()
                    .map(child -> transformNode(child, additionalProductions, generator))
                    .collect(Collectors.joining(" "));
        } else if (node instanceof AlternativeNode alt) {
            // Create a new non-terminal to represent the alternatives
            String newNonTerminal = generator.generate("alt");
            String rhs = alt.getAlternatives().stream()
                    .map(child -> transformNode(child, additionalProductions, generator))
                    .collect(Collectors.joining(" | "));
            additionalProductions.add(new EbnfProduction(newNonTerminal, rhs));
            return newNonTerminal;
        } else if (node instanceof OptionalNode opt) {
            // Optional: either inner or epsilon
            String newNonTerminal = generator.generate("opt");
            String inner = transformNode(opt.getNode(), additionalProductions, generator);
            additionalProductions.add(new EbnfProduction(newNonTerminal, inner + " | epsilon"));
            return newNonTerminal;
        } else if (node instanceof RepetitionNode rep) {
            // Repetition: recursive definition or epsilon
            String newNonTerminal = generator.generate("rep");
            String inner = transformNode(rep.getNode(), additionalProductions, generator);
            additionalProductions.add(new EbnfProduction(newNonTerminal,
                    inner + " " + newNonTerminal + " | epsilon"));
            return newNonTerminal;
        }
        // Fallback empty string for unsupported node types
        return "";
    }
}
