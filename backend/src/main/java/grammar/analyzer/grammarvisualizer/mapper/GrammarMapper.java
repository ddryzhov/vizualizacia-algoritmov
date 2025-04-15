package grammar.analyzer.grammarvisualizer.mapper;

import grammar.analyzer.grammarvisualizer.config.MapperConfig;
import grammar.analyzer.grammarvisualizer.dto.grammar.GrammarResponseDto;
import grammar.analyzer.grammarvisualizer.model.Grammar;
import org.mapstruct.Mapper;

/**
 * Mapper for converting Grammar entities to DTOs.
 */
@Mapper(config = MapperConfig.class)
public interface GrammarMapper {
    GrammarResponseDto toDto(Grammar grammar);
}
