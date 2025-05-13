package grammar.analyzer.grammarvisualizer.service;

import grammar.analyzer.grammarvisualizer.dto.grammar.GrammarRequestDto;
import grammar.analyzer.grammarvisualizer.dto.grammar.GrammarResponseDto;

/**
 * Service interface for performing full grammar analysis and retrieving step-by-step details.
 */
public interface GrammarService {
    /**
     * Analyzes the provided grammar input, computing FIRST, FOLLOW, and PREDICT sets,
     * and constructing the LL(1) parse table.
     *
     * @param grammarRequest DTO containing raw grammar string to analyze
     * @return DTO encapsulating complete analysis results
     */
    GrammarResponseDto analyzeGrammar(GrammarRequestDto grammarRequest);

    /**
     * Retrieves details for a specific analysis step or the LL(1) table for the previously analyzed grammar.
     *
     * @param analysisType type of analysis ("FIRST", "FOLLOW", "PREDICT", or "LL1")
     * @param stepIndex    zero-based index of the step to fetch; ignored for LL1
     * @param grammar      original grammar string
     * @return DTO containing step-specific or LL(1) detail results
     */
    GrammarResponseDto getStep(String analysisType, int stepIndex, String grammar);
}
