package grammar.analyzer.grammarvisualizer.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import grammar.analyzer.grammarvisualizer.dto.grammar.GrammarRequestDto;
import grammar.analyzer.grammarvisualizer.dto.grammar.GrammarResponseDto;
import grammar.analyzer.grammarvisualizer.exception.GrammarNotInitializedException;
import grammar.analyzer.grammarvisualizer.exception.UnknownAnalysisTypeException;
import grammar.analyzer.grammarvisualizer.mapper.GrammarMapper;
import grammar.analyzer.grammarvisualizer.model.Grammar;
import grammar.analyzer.grammarvisualizer.model.StepRecord;
import grammar.analyzer.grammarvisualizer.service.FirstFollowPredictService;
import grammar.analyzer.grammarvisualizer.service.GrammarParserService;
import grammar.analyzer.grammarvisualizer.service.LL1Service;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GrammarServiceImplTest {
    private GrammarMapper grammarMapper;
    private GrammarParserService grammarParserService;
    private FirstFollowPredictService firstFollowPredictService;
    private LL1Service ll1Service;
    private GrammarServiceImpl grammarService;

    @BeforeEach
    void setUp() {
        grammarMapper = mock(GrammarMapper.class);
        grammarParserService = mock(GrammarParserService.class);
        firstFollowPredictService = mock(FirstFollowPredictService.class);
        ll1Service = mock(LL1Service.class);
        grammarService = new GrammarServiceImpl(grammarMapper, grammarParserService,
                firstFollowPredictService, ll1Service);
    }

    @Test
    void testAnalyzeGrammar() {
        GrammarRequestDto requestDto = new GrammarRequestDto();
        requestDto.setGrammar("S -> 'a'");
        Grammar grammar = new Grammar();
        grammar.setProductionRules(Collections.singletonMap("S", Collections.singletonList("'a'")));
        when(grammarParserService.parseGrammar(anyString()))
                .thenReturn(grammar.getProductionRules());
        GrammarResponseDto responseDto = new GrammarResponseDto();
        when(grammarMapper.toDto(any(Grammar.class))).thenReturn(responseDto);
        GrammarResponseDto result = grammarService.analyzeGrammar(requestDto);
        assertSame(responseDto, result);
    }

    @Test
    void testGetStepLL1() {
        Grammar grammar = new Grammar();
        grammar.setLl1Table(Map.of("S", Map.of("$", "R1")));
        grammar.setLl1(true);
        grammar.setProductionRuleNumbers(Map.of("S -> 'a'", 1));
        grammar.setFirstStepRecords(List.of(new StepRecord("Step",
                Map.of(), 1)));

        grammarService.setCurrentGrammar(grammar);

        when(grammarMapper.toDto(grammar)).thenReturn(new GrammarResponseDto());

        GrammarResponseDto response = grammarService.getStep("LL1",
                0, "S -> 'a'");

        assertTrue(response.isLl1());
        assertEquals(0, response.getCurrentStepIndex());
        assertEquals(1, response.getTotalSteps());
    }

    @Test
    void testGetStepNonLL1() {
        Grammar grammar = new Grammar();
        grammar.setFirstStepRecords(List.of(
                new StepRecord("Step1", Map.of("S", Set.of("'a'")), 1),
                new StepRecord("Step2", Map.of("S", Set.of("'b'")), 2)
        ));
        grammarService.setCurrentGrammar(grammar);

        when(grammarMapper.toDto(grammar)).thenReturn(new GrammarResponseDto());

        GrammarResponseDto response = grammarService.getStep("FIRST",
                10, "S -> 'a'");
        assertEquals(1, response.getCurrentStepIndex());
        assertEquals(2, response.getTotalSteps());
        assertEquals(2, response.getPseudoCodeLine());
    }

    @Test
    void testGetStepUnknownAnalysisType() {
        Grammar grammar = new Grammar();
        grammar.setFirstStepRecords(List.of());
        grammarService.setCurrentGrammar(grammar);
        assertThrows(UnknownAnalysisTypeException.class,
                () -> grammarService.getStep("UNKNOWN", 0, "S -> 'a'"));
    }

    @Test
    void testGetStepWithoutGrammar() {
        grammarService.setCurrentGrammar(null);
        assertThrows(GrammarNotInitializedException.class,
                () -> grammarService.getStep("FIRST", 0, "S -> 'a'"));
    }
}
