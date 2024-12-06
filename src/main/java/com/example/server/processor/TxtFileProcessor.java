package com.example.server.processor;

//import org.apache.el.lang.ExpressionBuilder;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
//import java.beans.Expression;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TxtFileProcessor implements FileProcessor {

    @Override
    public String process(String content) {
        return recProc(content);
    }

    public String recProc(String content) {
        String bracketsReg = "\\(([^()]+)\\)";
        String defReg = "\\d+(\\s*[-+*/^]\\s*\\d+)*";
        Pattern brpat = Pattern.compile(bracketsReg);
        Matcher brmatch = brpat.matcher(content);
        while(brmatch.find()) {
            String expression = brmatch.group(1);
            String res = calculate(expression);
            content = content.replace( "(" + expression + ")" ,res);
            return recProc(content);
        }


        Pattern defpat = Pattern.compile(defReg);
        Matcher defmatch = defpat.matcher(content);
        while(defmatch.find()) {
            String expression = defmatch.group();
            String lastRes = calculate(expression);
            content = content.replace(expression,lastRes);
        }
        return content;
    }

    public String calculate (String content) {
        Expression expression = new ExpressionBuilder(content).build();
        double result = expression.evaluate();
        return " " + Double.toString(result);
    }
}