package com.example.server.expressionEvaluator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class countwithLibTest {
    @BeforeEach
    void setUp (){
        Object countwithLib = new countwithLib();
    }

    @Test
    void ExpressionDefault() {
        String input = "23+65/2";
        String expected = "55.5";
        String result = countwithLib.calculate(input);
        assertEquals(expected, result, "Значение не совпадает с ожидаемым");
    }

    @Test
    void ExpressionFault() {
        String input = "1/0";
        assertThrows(ArithmeticException.class, () -> countwithLib.calculate(input),
                "Метод должен выбрасывать исключение для некорректного выражения");
    }
}