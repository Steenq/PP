package com.example.server.processor;

import com.example.server.countmodels.searchCount;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.representer.Representer;

import java.util.*;

public class YamlFileProcessor implements FileProcessor {

    private final searchCount searchProcessor = new searchCount(); // Используем класс для обработки выражений

    @Override
    public String process(String content) {
        try {
            // Парсинг YAML в структуру данных (Map или List)
            LoaderOptions loaderOptions = new LoaderOptions();
            Yaml yaml = new Yaml(new SafeConstructor(loaderOptions));
            Object data = yaml.load(content);

            // Рекурсивная обработка структуры данных
            Object processedData = processYamlNode(data);

            // Преобразование обратно в YAML
            DumperOptions options = new DumperOptions();
            options.setPrettyFlow(true);
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            LoaderOptions loaderOptions1 = new LoaderOptions();
            Yaml dumper = new Yaml(new SafeConstructor(loaderOptions1));

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
