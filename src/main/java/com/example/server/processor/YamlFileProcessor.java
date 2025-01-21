package com.example.server.processor;

import com.example.server.expressionEvaluator.searchAndEvalExpressions;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import java.util.*;

public class YamlFileProcessor implements FileProcessor {

    private final searchAndEvalExpressions searchProcessor = new searchAndEvalExpressions(); // Используем класс для обработки выражений

    @Override
    public String process(String content) {
        try {
            // Парсинг YAML
            LoaderOptions loaderOptions = new LoaderOptions();
            Yaml yaml = new Yaml(new SafeConstructor(loaderOptions));
            Object data = yaml.load(content);

            // Обработка
            Object processedData = processYamlNode(data);

            // Преобразование обратно в YAML (блочный стиль)
            DumperOptions options = new DumperOptions();
            options.setPrettyFlow(true); // Читаемая форма
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); // Блочный стиль

            Yaml dumper = new Yaml(options); // Применяем настройки
            return dumper.dump(processedData);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка обработки YAML: " + e.getMessage(), e);
        }
    }

    private Object processYamlNode(Object node) {
        if (node instanceof Map) {
            // Рекурсивная обработка Map
            Map<Object, Object> map = (Map<Object, Object>) node;
            Map<Object, Object> processedMap = new LinkedHashMap<>();
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                processedMap.put(entry.getKey(), processYamlNode(entry.getValue()));
            }
            return processedMap;
        } else if (node instanceof List) {
            // Рекурсивная обработка List
            List<Object> list = (List<Object>) node;
            List<Object> processedList = new ArrayList<>();
            for (Object item : list) {
                processedList.add(processYamlNode(item));
            }
            return processedList;
        } else if (node instanceof String) {
            // Обработка строк
            String text = (String) node;
            return searchProcessor.methodLib(text);
        } else {
            return node;
        }
    }
}
