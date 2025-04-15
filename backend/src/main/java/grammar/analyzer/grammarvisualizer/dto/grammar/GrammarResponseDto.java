package grammar.analyzer.grammarvisualizer.dto.grammar;

import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for returning grammar analysis results.
 */
@Getter
@Setter
public class GrammarResponseDto {
    private boolean ll1;
    private Map<String, Set<String>> firstSets;
    private Map<String, Set<String>> followSets;
    private Map<String, Set<String>> predictSets;
    private Map<String, List<String>> productionRules;

    private Map<String, Map<String, String>> ll1Table;
    private String ll1Description;

    private Map<String, Set<String>> partialResult;
    private Map<String, List<String>> currentStepDetails;
    private String currentAnalysisType;
    private int pseudoCodeLine;

    private Integer totalSteps;
    private int currentStepIndex;

    private List<String> productionRuleList;
    private Map<String, Integer> productionRuleNumbers;

    private String transformedGrammar;
}

