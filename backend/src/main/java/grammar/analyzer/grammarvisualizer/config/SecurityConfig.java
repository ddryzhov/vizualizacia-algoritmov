package grammar.analyzer.grammarvisualizer.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Global CORS configuration for the application.
 * Defines allowed origins, HTTP methods, headers, and credential support.
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    /**
     * Creates and configures a CorsFilter bean with global settings.
     *
     * @return a configured CorsFilter
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // Origins permitted to access the API
        corsConfiguration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "https://ddryzhov.github.io",
                "https://vizualizacia-algoritmov-production.up.railway.app"
        ));

        // HTTP methods allowed for CORS requests
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Headers allowed in CORS requests
        corsConfiguration.setAllowedHeaders(List.of("*"));

        // Whether user credentials (cookies, authorization headers) are supported
        corsConfiguration.setAllowCredentials(true);

        // Apply CORS configuration to all endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }
}
