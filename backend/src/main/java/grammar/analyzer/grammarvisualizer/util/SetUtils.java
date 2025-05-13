package grammar.analyzer.grammarvisualizer.util;

import grammar.analyzer.grammarvisualizer.model.StepRecord;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for operations on sets and recording analysis steps.
 * Provides methods to initialize, copy, compare sets, and track computation steps.
 */
public class SetUtils {
    /**
     * Initializes a map mapping each key to an empty LinkedHashSet.
     *
     * @param keys set of keys for which to create empty sets
     * @return map of keys to empty sets
     */
    public static Map<String, Set<String>> initializeEmptySets(Set<String> keys) {
        Map<String, Set<String>> sets = new LinkedHashMap<>();
        for (String k : keys) {
            sets.put(k, new LinkedHashSet<>());
        }
        return sets;
    }

    /**
     * Creates a deep copy of the given map of sets.
     *
     * @param original map whose sets should be copied
     * @return new map with cloned sets preserving insertion order
     */
    public static Map<String, Set<String>> copySets(Map<String, Set<String>> original) {
        Map<String, Set<String>> copy = new LinkedHashMap<>();
        for (var e : original.entrySet()) {
            copy.put(e.getKey(), new LinkedHashSet<>(e.getValue()));
        }
        return copy;
    }

    /**
     * Compares two maps of sets for equality by keys and set contents.
     *
     * @param a first map to compare
     * @param b second map to compare
     * @return true if both maps have identical keys and equal sets; false otherwise
     */
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

    /**
     * Records a computation step by creating a StepRecord and appending to the steps list.
     *
     * @param description human-readable description of the step
     * @param snapshot    copy of current set state to visualize
     * @param steps       list collecting all step records
     * @param line        corresponding pseudocode line number for this step
     */
    public static void recordStep(String description, Map<String, Set<String>> snapshot,
                                  List<StepRecord> steps, int line) {
        steps.add(new StepRecord(description, snapshot, line));
    }
}
