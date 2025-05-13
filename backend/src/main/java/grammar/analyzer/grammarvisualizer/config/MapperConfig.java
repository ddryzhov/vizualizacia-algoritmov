package grammar.analyzer.grammarvisualizer.config;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.NullValueCheckStrategy;

/**
 * Central MapStruct configuration for all mappers in the application.
 * Uses Spring for component management, constructor injection for dependencies,
 * and always checks for null values before mapping.
 * Generated implementations are placed in the specified impl package.
 */
@org.mapstruct.MapperConfig(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        implementationPackage = "<PACKAGE_NAME>.impl"
)
public class MapperConfig {
}
