package grammar.analyzer.grammarvisualizer.config;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.NullValueCheckStrategy;

/**
 * This is a MapStruct configuration class that provides common settings for all mapper interfaces.
 */
@org.mapstruct.MapperConfig(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        implementationPackage = "<PACKAGE_NAME>.impl"
)
public class MapperConfig {
}
