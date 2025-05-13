package grammar.analyzer.grammarvisualizer.dto.grammar;

import lombok.Data;

/**
 * Data Transfer Object for requesting a specific step
 * in the grammar analysis process.
 */
@Data
public class GrammarStepRequestDto {
    private String analysisType;
    private int stepIndex;
    private String grammar;
}
