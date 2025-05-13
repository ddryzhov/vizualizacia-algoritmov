package grammar.analyzer.grammarvisualizer.dto.grammar;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for submitting grammar definitions.
 * Contains the raw grammar text to be analyzed.
 */
@Getter
@Setter
public class GrammarRequestDto {
    @NotBlank
    private String grammar;
}
