package grammar.analyzer.grammarvisualizer.dto.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class GrammarResponseDtoTest {
    @Test
    void testGettersAndSetters() {
        GrammarResponseDto dto = new GrammarResponseDto();

        dto.setLl1(true);
        assertTrue(dto.isLl1());

        Map<String, Set<String>> firstSets = new HashMap<>();
        dto.setFirstSets(firstSets);
        assertSame(firstSets, dto.getFirstSets());

        Map<String, Set<String>> followSets = new HashMap<>();
        dto.setFollowSets(followSets);
        assertSame(followSets, dto.getFollowSets());

        Map<String, Set<String>> predictSets = new HashMap<>();
        dto.setPredictSets(predictSets);
        assertSame(predictSets, dto.getPredictSets());

        Map<String, List<String>> productionRules = new HashMap<>();
        dto.setProductionRules(productionRules);
        assertSame(productionRules, dto.getProductionRules());

        Map<String, Map<String, String>> ll1Table = new HashMap<>();
        dto.setLl1Table(ll1Table);
        assertSame(ll1Table, dto.getLl1Table());

        dto.setLl1Description("LL1 desc");
        assertEquals("LL1 desc", dto.getLl1Description());

        Map<String, Set<String>> partialResult = new HashMap<>();
        dto.setPartialResult(partialResult);
        assertSame(partialResult, dto.getPartialResult());

        Map<String, List<String>> currentStepDetails = new HashMap<>();
        dto.setCurrentStepDetails(currentStepDetails);
        assertSame(currentStepDetails, dto.getCurrentStepDetails());

        dto.setCurrentAnalysisType("FIRST");
        assertEquals("FIRST", dto.getCurrentAnalysisType());

        dto.setPseudoCodeLine(7);
        assertEquals(7, dto.getPseudoCodeLine());

        dto.setTotalSteps(5);
        assertEquals(5, dto.getTotalSteps());

        dto.setCurrentStepIndex(2);
        assertEquals(2, dto.getCurrentStepIndex());

        List<String> productionRuleList = new ArrayList<>();
        dto.setProductionRuleList(productionRuleList);
        assertSame(productionRuleList, dto.getProductionRuleList());

        Map<String, Integer> productionRuleNumbers = new HashMap<>();
        dto.setProductionRuleNumbers(productionRuleNumbers);
        assertSame(productionRuleNumbers, dto.getProductionRuleNumbers());
    }
}
