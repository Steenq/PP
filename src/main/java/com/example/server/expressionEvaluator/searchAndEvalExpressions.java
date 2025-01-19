package com.example.server.expressionEvaluator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class searchAndEvalExpressions {
    public String methodLib(String content) {
        return recProcLib(content);
    }
    public String recProcLib(String content) {
        String bracketsReg = "\\(([^()]+)\\)";
        String defReg = "\\d+(\\s*[-+*/^]\\s*\\d+)*";
        Pattern brpat = Pattern.compile(bracketsReg);
        Matcher brmatch = brpat.matcher(content);

        // Обрабатываем выражения в скобках
        while (brmatch.find()) {
            String expression = brmatch.group(1);
            String res = new com.example.server.expressionEvaluator.countwithLib().calculate(expression);
            content = content.replaceFirst(Pattern.quote("(" + expression + ")"), res);
            return recProcLib(content); // Рекурсия для следующего уровня
        }

        // Обрабатываем оставшиеся выражения
        Pattern defpat = Pattern.compile(defReg);
        Matcher defmatch = defpat.matcher(content);
        while (defmatch.find()) {
            String expression = defmatch.group();
            String lastRes = new countwithLib().calculate(expression);
            content = content.replaceFirst(Pattern.quote(expression), lastRes);
        }
        return content;
    }
}
