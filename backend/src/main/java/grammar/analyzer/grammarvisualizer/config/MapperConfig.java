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
        componentModel = "spring",                              // Use Spring to manage mapper beans
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,      // Inject dependencies via constructor
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, // Always check for null before mapping
        implementationPackage = "<PACKAGE_NAME>.impl"           // Package for generated implementation classes
)
public class MapperConfig {
}
