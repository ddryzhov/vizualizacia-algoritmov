package grammar.analyzer.grammarvisualizer.service;

import grammar.analyzer.grammarvisualizer.dto.grammar.GrammarRequestDto;
import grammar.analyzer.grammarvisualizer.dto.grammar.GrammarResponseDto;

public interface GrammarService {
    GrammarResponseDto analyzeGrammar(GrammarRequestDto grammarRequest);

    GrammarResponseDto getStep(String analysisType, int stepIndex);
}
