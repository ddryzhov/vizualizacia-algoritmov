package grammar.analyzer.grammarvisualizer.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import grammar.analyzer.grammarvisualizer.model.StepRecord;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SetUtilsTest {
    @Test
    void testInitializeEmptySets() {
        Set<String> keys = new LinkedHashSet<>(Arrays.asList("A", "B"));
        Map<String, Set<String>> map = SetUtils.initializeEmptySets(keys);
        assertEquals(2, map.size());
        assertTrue(map.get("A").isEmpty());
        assertTrue(map.get("B").isEmpty());
    }

    @Test
    void testCopySets() {
        Map<String, Set<String>> original = new LinkedHashMap<>();
        original.put("A", new LinkedHashSet<>(List.of("1")));
        Map<String, Set<String>> copy = SetUtils.copySets(original);
        assertEquals(original, copy);
        copy.get("A").add("2");
        assertNotEquals(original, copy);
    }

    @Test
    void testEqualsSets() {
        Map<String, Set<String>> a = new LinkedHashMap<>();
        a.put("A", new LinkedHashSet<>(List.of("1")));
        Map<String, Set<String>> b = new LinkedHashMap<>();
        b.put("A", new LinkedHashSet<>(List.of("1")));
        assertTrue(SetUtils.equalsSets(a, b));
        b.get("A").add("2");
        assertFalse(SetUtils.equalsSets(a, b));
    }

    @Test
    void testRecordStep() {
        List<StepRecord> steps = new ArrayList<>();
        Map<String, Set<String>> snapshot = new LinkedHashMap<>();
        snapshot.put("A", new LinkedHashSet<>(List.of("1")));
        SetUtils.recordStep("Test step", snapshot, steps, 10);
        assertEquals(1, steps.size());
        StepRecord record = steps.get(0);
        assertEquals("Test step", record.getDescription());
        assertEquals(10, record.getPseudocodeLine());
        assertEquals(snapshot, record.getPartialResult());
    }
}
