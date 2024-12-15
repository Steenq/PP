package com.example.server.factory;

import com.example.server.processor.*;

public class FileProcessorFactory {

    public static FileProcessor getProcessor(String fileType) {
        switch (fileType.toLowerCase()) {
            case "txt":
                return new TxtFileProcessor();
            case "json":
                return new JsonFileProcessor();
            case "xml":
                return new XmlFileProcessor();
            case "yaml":
                return new YamlFileProcessor();
            default:
                throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
    }
}

