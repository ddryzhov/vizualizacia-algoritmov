package grammar.analyzer.grammarvisualizer.service.ebnf.transformer;

import grammar.analyzer.grammarvisualizer.service.EbnfTransformerService;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.ast.EbnfNode;
import grammar.analyzer.grammarvisualizer.service.ebnf.parser.EbnfParser;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service implementation for transforming EBNF grammar into BNF.
 * Processes each production line and appends additional
 * productions generated during the transformation.
 */
@Service
public class EbnfTransformerServiceImpl implements EbnfTransformerService {
    private int newNonTerminalCounter = 0;
    private List<EbnfProduction> additionalProductions;

    /**
     * Transforms the given EBNF grammar input into BNF format.
     *
     * @param grammarInput the EBNF grammar as a string
     * @return the transformed BNF grammar
     */
    @Override
    public String transform(String grammarInput) {
        newNonTerminalCounter = 0;
        additionalProductions = new ArrayList<>();

        StringBuilder transformedGrammar = new StringBuilder();
        String[] lines = grammarInput.split("\n");
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            int arrowIndex = line.indexOf("->");
            if (arrowIndex == -1) {
                transformedGrammar.append(line).append("\n");
                continue;
            }
            String lhs = line.substring(0, arrowIndex).trim();
            String rhs = line.substring(arrowIndex + 2).trim();

            EbnfParser parser = new EbnfParser(rhs);
            EbnfNode node = parser.parseExpression();

            String bnfRhs = EbnfNodeTransformer.transformNode(node, additionalProductions,
                    this::generateNewNonTerminal);
            transformedGrammar.append(lhs).append(" -> ").append(bnfRhs).append("\n");
        }

        for (EbnfProduction p : additionalProductions) {
            transformedGrammar.append(p.getLhs()).append(" -> ").append(p.getRhs()).append("\n");
        }

        return transformedGrammar.toString();
    }

    /**
     * Generates a new non-terminal name based on the provided prefix.
     *
     * @param prefix the prefix for the non-terminal name
     * @return a new unique non-terminal name
     */
    private String generateNewNonTerminal(String prefix) {
        return "_" + prefix + (++newNonTerminalCounter);
    }
}
