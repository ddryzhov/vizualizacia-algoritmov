package grammar.analyzer.grammarvisualizer.controller;

import grammar.analyzer.grammarvisualizer.dto.grammar.GrammarRequestDto;
import grammar.analyzer.grammarvisualizer.dto.grammar.GrammarResponseDto;
import grammar.analyzer.grammarvisualizer.dto.grammar.GrammarStepRequestDto;
import grammar.analyzer.grammarvisualizer.service.GrammarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing endpoints for grammar analysis
 * and step-by-step visualization of analysis results.
 */
@Tag(name = "Grammar Analysis",
        description = "Endpoints for grammar analysis and step-by-step visualization")
@RestController
@RequestMapping("/grammar")
@RequiredArgsConstructor
public class GrammarController {
    private final GrammarService grammarService;

    /**
     * Analyzes a provided grammar input, computing FIRST, FOLLOW, and PREDICT sets,
     * and determines if the grammar is LL(1).
     *
     * @param grammarRequest the grammar input DTO
     * @return DTO containing analysis results
     */
    @Operation(
            summary = "Analyze grammar",
            description = "Endpoint to analyze a given grammar "
                    + "and compute its FIRST, FOLLOW, and PREDICT sets. "
                    + "Also checks if the grammar is LL(1)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Grammar successfully analyzed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GrammarResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content
            )
    })
    @PostMapping("/analyze")
    public GrammarResponseDto analyzeGrammar(
            @Parameter(description = "Request containing the grammar input to analyze")
            @Valid @RequestBody GrammarRequestDto grammarRequest
    ) {
        return grammarService.analyzeGrammar(grammarRequest);
    }

    /**
     * Retrieves a specific step of the grammar analysis for FIRST, FOLLOW, or PREDICT,
     * based on step index and analysis type.
     *
     * @param requestDto DTO specifying analysis type, step index, and grammar
     * @return DTO with step-by-step analysis details
     */
    @Operation(
            summary = "Retrieve analysis step",
            description = "POST endpoint to request a specific "
                    + "step of grammar analysis for a given analysis type "
                    + "(FIRST, FOLLOW, PREDICT). The request body must include "
                    + "the analysis type, step index, and grammar."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Step details successfully retrieved",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GrammarResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Analysis step not found",
                    content = @Content
            )
    })
    @PostMapping("/step")
    public GrammarResponseDto getStep(
            @Valid @RequestBody GrammarStepRequestDto requestDto
    ) {
        return grammarService.getStep(
                requestDto.getAnalysisType(),
                requestDto.getStepIndex(),
                requestDto.getGrammar()
        );
    }
}
