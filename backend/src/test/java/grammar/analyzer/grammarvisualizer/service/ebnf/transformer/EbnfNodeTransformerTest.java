package grammar.analyzer.grammarvisualizer.service.ebnf.transformer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import grammar.analyzer.grammarvisualizer.service.ebnf.model.ast.AlternativeNode;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.ast.NonTerminalNode;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.ast.OptionalNode;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.ast.RepetitionNode;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.ast.SequenceNode;
import grammar.analyzer.grammarvisualizer.service.ebnf.model.ast.TerminalNode;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class EbnfNodeTransformerTest {
    @Test
    void testTransformTerminalNode() {
        TerminalNode terminalNode = new TerminalNode("'a'");
        List<EbnfProduction> additional = new ArrayList<>();
        String result = EbnfNodeTransformer.transformNode(terminalNode,
                additional, prefix -> "_" + prefix + "1");
        assertEquals("'a'", result);
        assertTrue(additional.isEmpty());
    }

    @Test
    void testTransformAlternativeNode() {
        TerminalNode node1 = new TerminalNode("'a'");
        TerminalNode node2 = new TerminalNode("'b'");
        List<EbnfProduction> additional = new ArrayList<>();
        AlternativeNode altNode = new AlternativeNode(List.of(node1, node2));
        String result = EbnfNodeTransformer.transformNode(altNode, additional,
                prefix -> "_" + prefix + "1");
        assertTrue(result.startsWith("_alt"));
        assertEquals(1, additional.size());
    }

    @Test
    void testTransformOptionalNode() {
        NonTerminalNode node = new NonTerminalNode("A");
        List<EbnfProduction> additional = new ArrayList<>();
        OptionalNode optNode = new OptionalNode(node);
        String result = EbnfNodeTransformer.transformNode(optNode, additional,
                prefix -> "_" + prefix + "1");
        assertTrue(result.startsWith("_opt"));
        assertEquals(1, additional.size());
        EbnfProduction prod = additional.get(0);
        assertTrue(prod.getRhs().contains("epsilon"));
    }

    @Test
    void testTransformRepetitionNode() {
        NonTerminalNode node = new NonTerminalNode("A");
        List<EbnfProduction> additional = new ArrayList<>();
        RepetitionNode repNode = new RepetitionNode(node);
        String result = EbnfNodeTransformer.transformNode(repNode, additional,
                prefix -> "_" + prefix + "1");
        assertTrue(result.startsWith("_rep"));
        assertEquals(1, additional.size());
        EbnfProduction prod = additional.get(0);
        assertTrue(prod.getRhs().contains("epsilon"));
    }

    @Test
    void testTransformSequenceNode() {
        TerminalNode node1 = new TerminalNode("'a'");
        NonTerminalNode node2 = new NonTerminalNode("A");
        List<EbnfProduction> additional = new ArrayList<>();
        SequenceNode seqNode = new SequenceNode(List.of(node1, node2));
        String result = EbnfNodeTransformer.transformNode(seqNode, additional,
                prefix -> "_" + prefix + "1");
        assertEquals("'a' A", result);
        assertTrue(additional.isEmpty());
    }
}
