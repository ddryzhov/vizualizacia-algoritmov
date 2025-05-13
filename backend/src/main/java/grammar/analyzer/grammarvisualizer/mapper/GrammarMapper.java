package grammar.analyzer.grammarvisualizer.mapper;

import grammar.analyzer.grammarvisualizer.config.MapperConfig;
import grammar.analyzer.grammarvisualizer.dto.grammar.GrammarResponseDto;
import grammar.analyzer.grammarvisualizer.model.Grammar;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper interface for converting Grammar entities
 * into their corresponding DTO representations.
 */
@Mapper(config = MapperConfig.class)
public interface GrammarMapper {
    /**
     * Maps a Grammar domain model object to a GrammarResponseDto.
     *
     * @param grammar the Grammar entity to convert
     * @return the populated GrammarResponseDto
     */
    GrammarResponseDto toDto(Grammar grammar);
}
