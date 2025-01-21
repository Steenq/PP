package com.example.server.expressionEvaluator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class searchAndEvalExpressionsTest {
    private searchAndEvalExpressions evaluator;
    @BeforeEach
    void setUp() {
        evaluator = new searchAndEvalExpressions();
    }
    @Test
    void testSimpleExpression() {
        String input = "2 + 3";
        String expected = "5";
        String result = evaluator.methodLib(input);
        assertEquals(expected, result, "Результат простого выражения не совпадает с ожидаемым");
    }

    @Test
    void testExpressionWithBrackets() {
        String input = "2 + (3 * 4)";
        String expected = "14";
        String result = evaluator.methodLib(input);
        assertEquals(expected, result, "Результат выражения со скобками не совпадает с ожидаемым");
    }

    @Test
    void testNestedBrackets() {
        String input = "(2 + (3 * 4)) - 5";
        String expected = "9";
        String result = evaluator.methodLib(input);
        assertEquals(expected, result, "Результат выражения с вложенными скобками не совпадает с ожидаемым");
    }

    @Test
    void testComplexExpression() {
        String input = "((2 + 3) * (4 - 1)) / 5";
        String expected = "3";
        String result = evaluator.methodLib(input);
        assertEquals(expected, result, "Результат сложного выражения не совпадает с ожидаемым");
    }

    @Test
    void testExpressionWithoutBrackets() {
        String input = "10 - 4 * 2";
        String expected = "2";
        String result = evaluator.methodLib(input);
        assertEquals(expected, result, "Результат выражения без скобок не совпадает с ожидаемым");
    }

    @Test
    void testEmptyExpression() {
        String input = "";
        String expected = "";
        String result = evaluator.methodLib(input);
        assertEquals(expected, result, "Пустая строка должна возвращать пустой результат");
    }

    @Test
    void testInvalidExpression() {
        String input = "2 + (3 * )";
        assertThrows(IllegalArgumentException.class, () -> evaluator.methodLib(input),
                "Метод должен выбрасывать исключение для некорректного выражения");
    }

    @Test
    void testNegativeNumbers() {
        String input = "-3 + (4 * -2)";
        String expected = "-11";
        String result = evaluator.methodLib(input);
        assertEquals(expected, result, "Результат выражения с отрицательными числами не совпадает с ожидаемым");
    }

    @Test
    void testDecimalNumbers() {
        String input = "(2.5 + 3.5) * 2";
        String expected = "12";
        String result = evaluator.methodLib(input);
        assertEquals(expected, result, "Результат выражения с десятичными числами не совпадает с ожидаемым");
    }
}
