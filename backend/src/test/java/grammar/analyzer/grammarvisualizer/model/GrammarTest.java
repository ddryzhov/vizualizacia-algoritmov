package grammar.analyzer.grammarvisualizer.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class GrammarTest {
    @Test
    void testGettersAndSetters() {
        Grammar grammar = new Grammar();

        Map<String, Set<String>> firstSets = new LinkedHashMap<>();
        grammar.setFirstSets(firstSets);
        assertSame(firstSets, grammar.getFirstSets());

        Map<String, Set<String>> followSets = new LinkedHashMap<>();
        grammar.setFollowSets(followSets);
        assertSame(followSets, grammar.getFollowSets());

        Map<String, Set<String>> predictSets = new LinkedHashMap<>();
        grammar.setPredictSets(predictSets);
        assertSame(predictSets, grammar.getPredictSets());

        Map<String, List<String>> productionRules = new LinkedHashMap<>();
        grammar.setProductionRules(productionRules);
        assertSame(productionRules, grammar.getProductionRules());

        List<StepRecord> firstStepRecords = new ArrayList<>();
        grammar.setFirstStepRecords(firstStepRecords);
        assertSame(firstStepRecords, grammar.getFirstStepRecords());

        List<StepRecord> followStepRecords = new ArrayList<>();
        grammar.setFollowStepRecords(followStepRecords);
        assertSame(followStepRecords, grammar.getFollowStepRecords());

        List<StepRecord> predictStepRecords = new ArrayList<>();
        grammar.setPredictStepRecords(predictStepRecords);
        assertSame(predictStepRecords, grammar.getPredictStepRecords());

        Map<String, Map<String, String>> ll1Table = new HashMap<>();
        grammar.setLl1Table(ll1Table);
        assertSame(ll1Table, grammar.getLl1Table());

        grammar.setLl1(true);
        assertTrue(grammar.isLl1());

        grammar.setPseudoCodeLine(10);
        assertEquals(10, grammar.getPseudoCodeLine());

        grammar.setCurrentAnalysisType("FIRST");
        assertEquals("FIRST", grammar.getCurrentAnalysisType());

        grammar.setCurrentStepIndex(2);
        assertEquals(2, grammar.getCurrentStepIndex());

        List<String> productionRuleList = new ArrayList<>();
        grammar.setProductionRuleList(productionRuleList);
        assertSame(productionRuleList, grammar.getProductionRuleList());

        Map<String, Integer> productionRuleNumbers = new HashMap<>();
        grammar.setProductionRuleNumbers(productionRuleNumbers);
        assertSame(productionRuleNumbers, grammar.getProductionRuleNumbers());
    }
}
