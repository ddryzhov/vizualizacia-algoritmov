package grammar.analyzer.grammarvisualizer.service.ebnf.transformer;

import static org.junit.jupiter.api.Assertions.assertTrue;

import grammar.analyzer.grammarvisualizer.service.EbnfTransformerService;
import org.junit.jupiter.api.Test;

class EbnfTransformerServiceImplTest {
    @Test
    void testTransformSimple() {
        EbnfTransformerService service = new EbnfTransformerServiceImpl();
        String grammarInput = "S -> 'a'";
        String result = service.transform(grammarInput);
        assertTrue(result.contains("S -> 'a'"));
    }

    @Test
    void testTransformWithEbnfConstructs() {
        EbnfTransformerService service = new EbnfTransformerServiceImpl();
        String grammarInput = "S -> [ 'a' ]";
        String result = service.transform(grammarInput);
        assertTrue(result.contains("S -> _opt1"));
        assertTrue(result.contains("_opt1 -> 'a' | epsilon"));
    }
}
