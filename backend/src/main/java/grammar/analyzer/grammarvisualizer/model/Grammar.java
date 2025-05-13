package grammar.analyzer.grammarvisualizer.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

/**
 * Domain model representing a grammar and its full analysis state.
 * Stores production rules, computed FIRST/FOLLOW/PREDICT sets,
 * LL(1) parsing table, and detailed step records for visualization.
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

    private Map<String, Map<String, String>> ll1Table;
    private boolean ll1;

    private int pseudoCodeLine;
    private String currentAnalysisType;

    private int currentStepIndex;

    private List<String> productionRuleList;
    private Map<String, Integer> productionRuleNumbers;

    private String transformedGrammar;
}
