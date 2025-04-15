package grammar.analyzer.grammarvisualizer.controller;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import grammar.analyzer.grammarvisualizer.dto.grammar.GrammarRequestDto;
import grammar.analyzer.grammarvisualizer.dto.grammar.GrammarResponseDto;
import grammar.analyzer.grammarvisualizer.dto.grammar.GrammarStepRequestDto;
import grammar.analyzer.grammarvisualizer.service.GrammarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GrammarControllerTest {
    private GrammarService grammarService;
    private GrammarController grammarController;

    @BeforeEach
    void setUp() {
        grammarService = mock(GrammarService.class);
        grammarController = new GrammarController(grammarService);
    }

    @Test
    void testAnalyzeGrammar() {
        GrammarRequestDto requestDto = new GrammarRequestDto();
        requestDto.setGrammar("S -> 'a'");
        GrammarResponseDto responseDto = new GrammarResponseDto();
        when(grammarService.analyzeGrammar(requestDto)).thenReturn(responseDto);
        GrammarResponseDto response = grammarController.analyzeGrammar(requestDto);
        assertSame(responseDto, response);
        verify(grammarService, times(1)).analyzeGrammar(requestDto);
    }

    @Test
    void testGetStep() {
        GrammarStepRequestDto requestDto = new GrammarStepRequestDto();
        requestDto.setAnalysisType("FIRST");
        requestDto.setStepIndex(0);
        requestDto.setGrammar("S -> 'a'");

        GrammarResponseDto responseDto = new GrammarResponseDto();
        when(grammarService.getStep("FIRST", 0, "S -> 'a'")).thenReturn(responseDto);

        GrammarResponseDto response = grammarController.getStep(requestDto);
        assertSame(responseDto, response);

        verify(grammarService, times(1)).getStep("FIRST", 0, "S -> 'a'");
    }
}
