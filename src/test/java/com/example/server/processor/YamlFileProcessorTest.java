package com.example.server.processor;

import com.example.server.expressionEvaluator.searchAndEvalExpressions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class YamlFileProcessorTest {

    private YamlFileProcessor yamlFileProcessor;
    private searchAndEvalExpressions searchProcessor;

    @BeforeEach
    void setUp() {
        searchProcessor = new searchAndEvalExpressions(); // Реальный экземпляр searchAndEvalExpressions
        yamlFileProcessor = new YamlFileProcessor(); // Реальный YamlFileProcessor
    }

    @Test
    void testProcessValidYaml() {
        String inputYaml = "key: value 2+3\n" +
                "list:\n" +
                "  - item1\n" +
                "  - item2\n" +
                "number: 123\n";
        String expectedProcessedValue = searchProcessor.methodLib("value 2+3");
        String expectedOutputYaml = "key: " + expectedProcessedValue + "\n" +
                "list:\n" +
                "- item1\n" +
                "- item2\n" +
                "number: 123\n";

        String result = yamlFileProcessor.process(inputYaml);

        assertEquals(expectedOutputYaml, result);
    }

    @Test
    void testProcessInvalidYaml() {
        String invalidYaml = "key: value\nlist\n  - item1";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> yamlFileProcessor.process(invalidYaml)
        );

        assertTrue(exception.getMessage().contains("Ошибка обработки YAML"));
    }
}
