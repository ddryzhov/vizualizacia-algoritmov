package grammar.analyzer.grammarvisualizer.dto.grammar;

import lombok.Data;

/**
 * DTO for a grammar analysis step request.
 */
@Data
public class GrammarStepRequestDto {
    private String analysisType;
    private int stepIndex;
    private String grammar;
}
