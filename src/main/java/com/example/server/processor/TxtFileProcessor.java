package com.example.server.processor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TxtFileProcessor implements FileProcessor {

    @Override
    public String process(String content) {
        // Логика обработки текста
        String regex = "(\\d+(\\s*[-+*/^]\\s*\\d+)*(\\s*\\(.*?\\))*)";


        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        int newStart = 0;
        int newEnd = 0;
        while (matcher.find()) {

            String expression = matcher.group();

            int start = matcher.start();
            int end = matcher.end();

            String res = "answer";
            StringBuilder newRes = new StringBuilder(content);
            newRes.replace(start + newStart,end + newEnd,res);
            newStart += res.length() - expression.length();
            newEnd += res.length() - expression.length();
            content = newRes.toString();
        }
        return content;
    }
}