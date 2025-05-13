package grammar.analyzer.grammarvisualizer.service.impl;

import grammar.analyzer.grammarvisualizer.dto.grammar.GrammarRequestDto;
import grammar.analyzer.grammarvisualizer.dto.grammar.GrammarResponseDto;
import grammar.analyzer.grammarvisualizer.exception.GrammarNotInitializedException;
import grammar.analyzer.grammarvisualizer.exception.UnknownAnalysisTypeException;
import grammar.analyzer.grammarvisualizer.mapper.GrammarMapper;
import grammar.analyzer.grammarvisualizer.model.Grammar;
import grammar.analyzer.grammarvisualizer.model.StepRecord;
import grammar.analyzer.grammarvisualizer.service.FirstFollowPredictService;
import grammar.analyzer.grammarvisualizer.service.GrammarParserService;
import grammar.analyzer.grammarvisualizer.service.GrammarService;
import grammar.analyzer.grammarvisualizer.service.LL1Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Core service for grammar analysis, providing methods to analyze a grammar and retrieve step details.
 * Coordinates parsing, computing FIRST/FOLLOW/PREDICT sets, and building the LL(1) table.
 */
@Service
@RequiredArgsConstructor
public class GrammarServiceImpl implements GrammarService {
    private final GrammarMapper grammarMapper;
    private final GrammarParserService grammarParserService;
    private final FirstFollowPredictService firstFollowPredictService;
    private final LL1Service ll1Service;
    /**
     * -- GETTER --
     *  For test purposes only.
     * -- SETTER --
     *  For test purposes only.
     */
    @Setter
    @Getter
    private Grammar currentGrammar;

    /**
     * Analyzes input grammar and returns full analysis results.
     * Caches results by grammar text for performance.
     *
     * @param grammarRequest DTO containing raw grammar string
     * @return DTO with FIRST/FOLLOW/PREDICT sets and LL(1) table
     */
    @Cacheable(value = "grammarCache", key = "#grammarRequest.grammar")
    @Override
    public GrammarResponseDto analyzeGrammar(GrammarRequestDto grammarRequest) {
        currentGrammar = performAnalysis(grammarRequest.getGrammar());
        currentGrammar.setCurrentAnalysisType("FIRST");
        return grammarMapper.toDto(currentGrammar);
    }

    /**
     * Retrieves details for a specific analysis step or the LL(1) table.
     *
     * @param analysisType type of analysis: FIRST, FOLLOW, PREDICT, or LL1
     * @param stepIndex    zero-based index of the step to fetch
     * @param grammar      original grammar string
     * @return DTO with step-specific details or LL(1) info
     */
    @Override
    public GrammarResponseDto getStep(String analysisType, int stepIndex, String grammar) {
        checkGrammarInitialized();
        GrammarResponseDto response = grammarMapper.toDto(currentGrammar);

        if ("LL1".equalsIgnoreCase(analysisType)) {
            response.setLl1Table(currentGrammar.getLl1Table());
            response.setLl1(currentGrammar.isLl1());
            response.setCurrentStepDetails(Map.of("details",
                    List.of("LL(1) table and PREDICT rules shown below.")));
            response.setTotalSteps(1);
            response.setCurrentStepIndex(0);
        } else {
            List<StepRecord> stepRecords = getStepRecordsForType(analysisType, currentGrammar);
            int totalSteps = stepRecords.size();

            stepIndex = Math.max(0, Math.min(stepIndex, totalSteps - 1));

            StepRecord currentStep = stepRecords.get(stepIndex);
            response.setPartialResult(currentStep.getPartialResult());
            response.setCurrentStepDetails(Map.of("details",
                    List.of(currentStep.getDescription())));
            response.setPseudoCodeLine(currentStep.getPseudocodeLine());

            response.setCurrentStepIndex(stepIndex);
            response.setTotalSteps(totalSteps);
        }

        return response;
    }

    /**
     * Runs the full pipeline: parsing, computing sets, and LL(1) table construction.
     *
     * @param grammarInput raw grammar definition
     * @return fully populated Grammar domain model
     */
    private Grammar performAnalysis(String grammarInput) {
        Grammar grammar = new Grammar();
        grammar.setProductionRules(grammarParserService.parseGrammar(grammarInput));
        var nonTerminals = grammar.getProductionRules().keySet();
        final String startSymbol = nonTerminals.iterator().next();

        List<String> rules = new ArrayList<>();
        Map<String, Integer> ruleNumbers = new HashMap<>();
        int ruleIndex = 1;
        for (Map.Entry<String, List<String>> entry : grammar.getProductionRules().entrySet()) {
            String lhs = entry.getKey();
            for (String rhs : entry.getValue()) {
                String fullRule = lhs + " -> " + rhs;
                rules.add(fullRule);
                ruleNumbers.put(fullRule, ruleIndex++);
            }
        }
        grammar.setProductionRuleList(rules);
        grammar.setProductionRuleNumbers(ruleNumbers);
        grammar.setTransformedGrammar(
                grammar.getProductionRules().entrySet().stream()
                        .map(e -> e.getKey() + " -> " + String.join(" | ", e.getValue()))
                        .collect(Collectors.joining("\n"))
        );

        // Compute FIRST, FOLLOW, and PREDICT sets
        firstFollowPredictService.computeFirstSets(grammar.getProductionRules(),
                nonTerminals, grammar);
        firstFollowPredictService.computeFollowSets(grammar.getProductionRules(),
                grammar.getFirstSets(), nonTerminals, startSymbol, grammar);
        firstFollowPredictService.computePredictSets(grammar.getProductionRules(),
                grammar.getFirstSets(), grammar.getFollowSets(), grammar);

        // Build LL(1) parse table
        ll1Service.buildLl1Table(grammar.getProductionRules(),
                grammar.getPredictSets(), grammar);

        return grammar;
    }

    /**
     * Throws if no grammar has been analyzed before fetching steps.
     */
    private void checkGrammarInitialized() {
        if (currentGrammar == null) {
            throw new GrammarNotInitializedException("No grammar has been analyzed yet.");
        }
    }

    /**
     * Returns the list of step records for the specified analysis type.
     *
     * @param analysisType    analysis type identifier
     * @param grammar analyzed Grammar model
     * @return list of step records
     */
    private List<StepRecord> getStepRecordsForType(String analysisType, Grammar grammar) {
        return switch (analysisType.toUpperCase()) {
            case "FIRST" -> grammar.getFirstStepRecords();
            case "FOLLOW" -> grammar.getFollowStepRecords();
            case "PREDICT" -> grammar.getPredictStepRecords();
            default -> throw new UnknownAnalysisTypeException(analysisType);
        };
    }
}
