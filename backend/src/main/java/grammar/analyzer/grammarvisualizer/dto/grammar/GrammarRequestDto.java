package grammar.analyzer.grammarvisualizer.dto.grammar;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GrammarRequestDto {
    @NotBlank
    private String grammar;
}
