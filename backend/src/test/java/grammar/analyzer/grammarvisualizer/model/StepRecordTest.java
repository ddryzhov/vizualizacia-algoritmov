package grammar.analyzer.grammarvisualizer.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class StepRecordTest {
    @Test
    void testConstructorAndGetters() {
        Map<String, Set<String>> partialResult = new HashMap<>();
        StepRecord record = new StepRecord("My description", partialResult, 42);

        assertEquals("My description", record.getDescription());
        assertSame(partialResult, record.getPartialResult());
        assertEquals(42, record.getPseudocodeLine());
    }
}
