package grammar.analyzer.grammarvisualizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class GrammarVisualizerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrammarVisualizerApplication.class, args);
    }

}
