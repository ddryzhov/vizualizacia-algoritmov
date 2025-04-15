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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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
     * Analyze the given grammar input and compute its FIRST, FOLLOW, and PREDICT sets.
     * Additionally, build the LL(1) table for the grammar.
     *
     * @param grammarRequest the request DTO containing the grammar input
     * @return a response DTO containing the analysis results
     */
    @Cacheable(value = "grammarCache", key = "#grammarRequest.grammar")
    @Override
    public GrammarResponseDto analyzeGrammar(GrammarRequestDto grammarRequest) {
        currentGrammar = performAnalysis(grammarRequest.getGrammar());
        currentGrammar.setCurrentAnalysisType("FIRST");
        return grammarMapper.toDto(currentGrammar);
    }

    /**
     * Retrieve the step details for the given analysis type and step index.
     *
     * @param analysisType the type of analysis (e.g., "FIRST", "FOLLOW", "PREDICT")
     * @param stepIndex    the index of the step to retrieve
     * @return a response DTO containing the details of the requested step
     */
    @Override
    public GrammarResponseDto getStep(String analysisType, int stepIndex, String grammar) {
        checkGrammarInitialized();
        GrammarResponseDto response = grammarMapper.toDto(currentGrammar);

        if ("LL1".equalsIgnoreCase(analysisType)) {
            response.setLl1Table(currentGrammar.getLl1Table());
            response.setLl1Description(currentGrammar.getLl1Description());
            response.setLl1(currentGrammar.isLl1());
            response.setCurrentStepDetails(Map.of("details",
                    List.of("LL(1) table and PREDICT rules shown below.")));
            response.setTotalSteps(1); // LL1 всегда один шаг
            response.setCurrentStepIndex(0);
        } else {
            List<StepRecord> stepRecords = getStepRecordsForType(analysisType, currentGrammar);
            int totalSteps = stepRecords.size();

            // Clamp stepIndex clearly here
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
     * Perform a full analysis of the grammar.
     * This method computes the FIRST, FOLLOW, and PREDICT sets, as well as builds the LL(1) table.
     *
     * @param grammarInput the input string representing the grammar
     * @return the fully analyzed Grammar object
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

        firstFollowPredictService.computeFirstSets(grammar.getProductionRules(),
                nonTerminals, grammar);
        firstFollowPredictService.computeFollowSets(grammar.getProductionRules(),
                grammar.getFirstSets(), nonTerminals, startSymbol, grammar);
        firstFollowPredictService.computePredictSets(grammar.getProductionRules(),
                grammar.getFirstSets(), grammar.getFollowSets(), grammar);

        ll1Service.buildLL1TableWithSteps(grammar.getProductionRules(),
                grammar.getPredictSets(), grammar);

        return grammar;
    }

    /**
     * Check if a grammar has been analyzed. If not, throw an exception.
     *
     * @throws IllegalStateException if no grammar has been analyzed
     */
    private void checkGrammarInitialized() {
        if (currentGrammar == null) {
            throw new GrammarNotInitializedException("No grammar has been analyzed yet.");
        }
    }

    /**
     * Retrieve step records for the specified analysis type.
     *
     * @param analysisType the type of analysis (e.g., "FIRST", "FOLLOW", "PREDICT")
     * @param grammar      the analyzed Grammar object
     * @return a list of StepRecord objects for the specified analysis type
     * @throws IllegalArgumentException if the analysis type is unknown
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
