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
 * REST controller for grammar analysis and step-by-step visualization.
 */
@Tag(name = "Grammar Analysis",
        description = "Endpoints for grammar analysis and step-by-step visualization")
@RestController
@RequestMapping("/grammar")
@RequiredArgsConstructor
public class GrammarController {
    private final GrammarService grammarService;

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
