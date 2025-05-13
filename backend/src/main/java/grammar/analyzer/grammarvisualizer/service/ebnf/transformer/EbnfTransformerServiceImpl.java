package grammar.analyzer.grammarvisualizer.service.ebnf.transformer;

import grammar.analyzer.grammarvisualizer.service.EbnfTransformerService;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.ast.EbnfNode;
import grammar.analyzer.grammarvisualizer.service.ebnf.parser.EbnfParser;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service that converts EBNF grammar definitions into BNF format.
 * Reads each production line, parses the RHS, and applies AST transformations
 * to expand EBNF constructs into equivalent BNF productions.
 */
@Service
public class EbnfTransformerServiceImpl implements EbnfTransformerService {
    private int newNonTerminalCounter = 0;
    private List<EbnfProduction> additionalProductions;

    /**
     * Transforms an EBNF grammar string into its BNF equivalent.
     * Initializes state, processes each input production, and appends any
     * additional productions created for alternatives, optionals, and repetitions.
     *
     * @param grammarInput multiline EBNF grammar text
     * @return multiline BNF grammar string
     */
    @Override
    public String transform(String grammarInput) {
        newNonTerminalCounter = 0; // Reset counter for unique name generation
        additionalProductions = new ArrayList<>(); // Collect generated productions

        StringBuilder transformedGrammar = new StringBuilder();
        String[] lines = grammarInput.split("\n");
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue; // Skip blank lines
            }
            int arrowIndex = line.indexOf("->");
            if (arrowIndex == -1) {
                transformedGrammar.append(line).append("\n"); // Pass through non-production lines
                continue;
            }

            // Split LHS and RHS of the production
            String lhs = line.substring(0, arrowIndex).trim();
            String rhs = line.substring(arrowIndex + 2).trim();

            // Parse RHS into AST
            EbnfParser parser = new EbnfParser(rhs);
            EbnfNode node = parser.parseExpression();

            // Transform AST into BNF fragment, collecting extra productions
            String bnfRhs = EbnfNodeTransformer.transformNode(node, additionalProductions,
                    this::generateNewNonTerminal);
            transformedGrammar.append(lhs).append(" -> ").append(bnfRhs).append("\n");
        }

        // Append all helper productions generated during transformation
        for (EbnfProduction p : additionalProductions) {
            transformedGrammar.append(p.getLhs()).append(" -> ").append(p.getRhs()).append("\n");
        }

        return transformedGrammar.toString();
    }

    /**
     * Generates a unique non-terminal name using the given prefix.
     *
     * @param prefix base prefix for the generated name
     * @return unique non-terminal string beginning with '_' and the prefix
     */
    private String generateNewNonTerminal(String prefix) {
        return "_" + prefix + (++newNonTerminalCounter);
    }
}
