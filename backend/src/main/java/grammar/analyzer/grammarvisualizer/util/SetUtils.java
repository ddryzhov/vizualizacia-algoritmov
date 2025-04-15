package grammar.analyzer.grammarvisualizer.util;

import grammar.analyzer.grammarvisualizer.model.StepRecord;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for set operations and step recording.
 */
public class SetUtils {
    /**
     * Initializes a map with empty sets for each key.
     *
     * @param keys the keys to initialize
     * @return a map with each key mapped to an empty set
     */
    public static Map<String, Set<String>> initializeEmptySets(Set<String> keys) {
        Map<String, Set<String>> sets = new LinkedHashMap<>();
        for (String k : keys) {
            sets.put(k, new LinkedHashSet<>());
        }
        return sets;
    }

    /**
     * Creates a deep copy of a map of sets.
     *
     * @param original the original map to copy
     * @return a deep copy of the map
     */
    public static Map<String, Set<String>> copySets(Map<String, Set<String>> original) {
        Map<String, Set<String>> copy = new LinkedHashMap<>();
        for (var e : original.entrySet()) {
            copy.put(e.getKey(), new LinkedHashSet<>(e.getValue()));
        }
        return copy;
    }

    /**
     * Compares two maps of sets for equality.
     *
     * @param a the first map
     * @param b the second map
     * @return true if the maps are equal; false otherwise
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
     * Records a step in the analysis process.
     *
     * @param description a description of the step
     * @param snapshot    a snapshot of the current state
     * @param steps       the list to which the step is added
     * @param line        the pseudocode line number corresponding to the step
     */
    public static void recordStep(String description, Map<String, Set<String>> snapshot,
                                  List<StepRecord> steps, int line) {
        steps.add(new StepRecord(description, snapshot, line));
    }
}
