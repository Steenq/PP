package com.example.server.processor;

import com.example.server.expressionEvaluator.searchAndEvalExpressions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TxtFileProcessorTest {

    private TxtFileProcessor txtFileProcessor;
    private searchAndEvalExpressions searchProcessor;

    @BeforeEach
    void setUp() {
        searchProcessor = new searchAndEvalExpressions();
        txtFileProcessor = new TxtFileProcessor();
    }

    @Test
    void testProcess() {
        String input = "Привет, у меня есть 3+1 яблока и я не знаю что с ними делать.\n" +
                "Помоги раздать каждому бедному африканцу по ((5/5)+1.1) яблоку.";
        String expectedOutput = searchProcessor.methodLib(input);

        String result = txtFileProcessor.process(input);

        assertEquals(expectedOutput, result);
    }
}
