package com.example.server.processor;

import com.example.server.expressionEvaluator.searchAndEvalExpressions;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.JsonNode;
import static org.junit.jupiter.api.Assertions.*;

class JsonFileProcessorTest {

    private JsonFileProcessor jsonFileProcessor;
    private searchAndEvalExpressions searchProcessor;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        searchProcessor = new searchAndEvalExpressions(); // Реальный экземпляр searchCount
        jsonFileProcessor = new JsonFileProcessor(); // Реальный JsonFileProcessor
        objectMapper = new ObjectMapper();
    }



    @Test
    void testProcessValidJson() throws Exception {
        String inputJson = "{\n" +
                "  \"array\": [\n" +
                "    1,\n" +
                "    2,\n" +
                "    3\n" +
                "  ],\n" +
                "  \"boolean\": true,\n" +
                "  \"color\": \"gold 123-12\",\n" +
                "  \"null\": null,\n" +
                "  \"number\": 123,\n" +
                "  \"object\": {\n" +
                "    \"a\": \"b\",\n" +
                "    \"c\": \"d\"\n" +
                "  },\n" +
                "  \"string\": \"Hello World\"\n" +
                "}";

        String expectedProcessedValue = searchProcessor.methodLib("gold 123-12");
        String expectedOutputJson = "{\n" +
                "  \"array\": [\n" +
                "    1,\n" +
                "    2,\n" +
                "    3\n" +
                "  ],\n" +
                "  \"boolean\": true,\n" +
                "  \"color\": \"" + expectedProcessedValue + "\",\n" +
                "  \"null\": null,\n" +
                "  \"number\": 123,\n" +
                "  \"object\": {\n" +
                "    \"a\": \"b\",\n" +
                "    \"c\": \"d\"\n" +
                "  },\n" +
                "  \"string\": \"Hello World\"\n" +
                "}";

        // Преобразуем строки JSON в объекты JsonNode для сравнения
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualJson = mapper.readTree(jsonFileProcessor.process(inputJson));
        JsonNode expectedJson = mapper.readTree(expectedOutputJson);

        assertEquals(expectedJson, actualJson);
    }


    @Test
    void testProcessInvalidJson() {
        String invalidJson = "{ invalid json }";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> jsonFileProcessor.process(invalidJson)
        );

        assertTrue(exception.getMessage().contains("Ошибка обработки JSON"));
    }
}
