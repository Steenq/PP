package com.example.server.processor;

import com.example.server.countmodels.searchCount;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class JsonFileProcessor implements FileProcessor {

    private final searchCount searchProcessor = new searchCount(); // Экземпляр класса searchCount

    @Override
    public String process(String content) {
        try {
            // Парсинг JSON с помощью Jackson
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(content);

            // Обработка JSON-узлов
            JsonNode processedNode = processJsonNode(rootNode);

            // Преобразование обратно в строку JSON
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(processedNode);
        } catch (IOException e) {
            throw new IllegalArgumentException("Ошибка обработки JSON: " + e.getMessage(), e);
        }
    }

    private JsonNode processJsonNode(JsonNode node) {
        if (node.isObject()) {
            // Обработка объектов JSON
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                JsonNode processedChild = processJsonNode(field.getValue());
                ((ObjectNode) node).set(field.getKey(), processedChild);
            }
        } else if (node.isArray()) {
            // Обработка массивов JSON
            for (int i = 0; i < node.size(); i++) {
                JsonNode processedChild = processJsonNode(node.get(i));
                ((ArrayNode) node).set(i, processedChild);
            }
        } else if (node.isTextual()) {
            // Если узел — текст (строка), обрабатываем через searchCount
            String text = node.asText();
            String processedText = searchProcessor.methodLib(text);
            return new TextNode(processedText);
        } else if (node.isNumber()) {
            // Если узел — число, возвращаем как есть
            return node;
        }
        return node;
    }
}
