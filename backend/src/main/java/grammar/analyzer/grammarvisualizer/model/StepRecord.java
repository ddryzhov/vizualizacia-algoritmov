package grammar.analyzer.grammarvisualizer.model;

import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Model representing a single analysis step in the grammar processing pipeline.
 * Contains a description, intermediate result sets, and the related pseudocode line.
 */
@Getter
@Setter
@AllArgsConstructor
public class StepRecord {
    private String description;

    private Map<String, Set<String>> partialResult;

    private int pseudocodeLine;
}
