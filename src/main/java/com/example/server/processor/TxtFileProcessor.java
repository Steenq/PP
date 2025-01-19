package com.example.server.processor;

import com.example.server.expressionEvaluator.searchAndEvalExpressions;

public class TxtFileProcessor implements FileProcessor {

    @Override
    public String process(String content) {
        String res = new searchAndEvalExpressions().methodLib(content);
        return res;
    }
}
