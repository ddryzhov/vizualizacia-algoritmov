package grammar.analyzer.grammarvisualizer.model;

import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a single step in the analysis process.
 */
@Getter
@Setter
@AllArgsConstructor
public class StepRecord {
    private String description;

    private Map<String, Set<String>> partialResult;

    private int pseudocodeLine;
}
