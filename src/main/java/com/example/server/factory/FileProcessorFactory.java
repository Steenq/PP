package com.example.server.factory;

import com.example.server.processor.FileProcessor;
import com.example.server.processor.TxtFileProcessor;
import com.example.server.processor.XmlFileProcessor;

public class FileProcessorFactory {

    public static FileProcessor getProcessor(String fileType) {
        switch (fileType.toLowerCase()) {
            case "txt":
                return new TxtFileProcessor();
            case "xml":
                return new XmlFileProcessor();
            default:
                throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
    }
}

