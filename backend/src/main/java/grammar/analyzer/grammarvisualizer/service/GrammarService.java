package grammar.analyzer.grammarvisualizer.service;

import grammar.analyzer.grammarvisualizer.dto.grammar.GrammarRequestDto;
import grammar.analyzer.grammarvisualizer.dto.grammar.GrammarResponseDto;

/**
 * Service interface for grammar analysis and step retrieval.
 */
public interface GrammarService {
    GrammarResponseDto analyzeGrammar(GrammarRequestDto grammarRequest);

    GrammarResponseDto getStep(String analysisType, int stepIndex, String grammar);
}
