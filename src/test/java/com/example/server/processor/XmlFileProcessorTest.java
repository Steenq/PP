package com.example.server.processor;

import com.example.server.expressionEvaluator.searchAndEvalExpressions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class XmlFileProcessorTest {

    private XmlFileProcessor xmlFileProcessor;
    private searchAndEvalExpressions searchProcessor;

    @BeforeEach
    void setUp() {
        searchProcessor = new searchAndEvalExpressions(); // Реальный экземпляр searchAndEvalExpressions
        xmlFileProcessor = new XmlFileProcessor(); // Реальный XmlFileProcessor
    }

    @Test
    void testProcessValidXml() {
        String inputXml = "<root><item>text to process 3*3</item><number>123</number></root>";
        String expectedProcessedText = searchProcessor.methodLib("text to process 3*3");
        String expectedOutputXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
                "<root><item>" + expectedProcessedText + "</item><number>123</number></root>";

        String result = xmlFileProcessor.process(inputXml);

        assertEquals(expectedOutputXml, result);
    }

    @Test
    void testProcessInvalidXml() {
        String invalidXml = "<root><item>text</item";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> xmlFileProcessor.process(invalidXml)
        );

        assertTrue(exception.getMessage().contains("Ошибка обработки XML"));
    }
}
