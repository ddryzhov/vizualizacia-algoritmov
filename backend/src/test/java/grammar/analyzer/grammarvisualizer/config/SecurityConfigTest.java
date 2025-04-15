package grammar.analyzer.grammarvisualizer.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.web.filter.CorsFilter;

class SecurityConfigTest {
    @Test
    void testCorsFilter() {
        SecurityConfig config = new SecurityConfig();
        CorsFilter corsFilter = config.corsFilter();
        assertNotNull(corsFilter);
    }
}
