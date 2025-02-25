package grammar.analyzer.grammarvisualizer.util;

import grammar.analyzer.grammarvisualizer.model.StepRecord;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SetUtils {
    public static Map<String, Set<String>> initializeEmptySets(Set<String> keys) {
        Map<String, Set<String>> sets = new LinkedHashMap<>();
        for (String k : keys) {
            sets.put(k, new LinkedHashSet<>());
        }
        return sets;
    }

    public static Map<String, Set<String>> copySets(Map<String, Set<String>> original) {
        Map<String, Set<String>> copy = new LinkedHashMap<>();
        for (var e : original.entrySet()) {
            copy.put(e.getKey(), new LinkedHashSet<>(e.getValue()));
        }
        return copy;
    }

    public static boolean equalsSets(Map<String, Set<String>> a, Map<String, Set<String>> b) {
        if (a.size() != b.size()) {
            return false;
        }
        for (String key : a.keySet()) {
            if (!b.containsKey(key)) {
                return false;
            }
            if (!a.get(key).equals(b.get(key))) {
                return false;
            }
        }
        return true;
    }

    public static void recordStep(String description, Map<String, Set<String>> snapshot,
                                  List<StepRecord> steps, int line) {
        steps.add(new StepRecord(description, snapshot, line));
    }
}
