package com.example.server.processor;

import com.example.server.countmodels.searchCount;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TxtFileProcessor implements FileProcessor {

    @Override
    public String process(String content) {
        String res = new searchCount().methodLib(content);
        return res;
    }
}
