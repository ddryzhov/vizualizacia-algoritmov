package grammar.analyzer.grammarvisualizer.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a grammar and stores its analysis results.
 * Contains production rules, computed sets (FIRST, FOLLOW, PREDICT), LL(1) table,
 * and step records for the analysis process.
 */
@Getter
@Setter
public class Grammar {
    private Map<String, Set<String>> firstSets = new LinkedHashMap<>();
    private Map<String, Set<String>> followSets = new LinkedHashMap<>();
    private Map<String, Set<String>> predictSets = new LinkedHashMap<>();
    private Map<String, List<String>> productionRules = new LinkedHashMap<>();

    private List<StepRecord> firstStepRecords;
    private List<StepRecord> followStepRecords;
    private List<StepRecord> predictStepRecords;

    private String ll1Description;
    private Map<String, Map<String, String>> ll1Table;
    private boolean ll1;

    private int pseudoCodeLine;
    private String currentAnalysisType;

    private int currentStepIndex;

    private List<String> productionRuleList;
    private Map<String, Integer> productionRuleNumbers;

    private String transformedGrammar;
}
