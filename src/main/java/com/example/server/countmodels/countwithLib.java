package com.example.server.countmodels;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class countwithLib{
    public String calculate(String content) {
        Expression expression = new ExpressionBuilder(content).build();
        double result = expression.evaluate();

        // Преобразуем результат в строку без лишних ".0" для целых чисел
        return (result % 1 == 0) ? String.valueOf((int) result) : String.valueOf(result);
    }
}
