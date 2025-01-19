package com.example.server.processor;

import com.example.server.expressionEvaluator.searchAndEvalExpressions;
import org.w3c.dom.*;

import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class XmlFileProcessor implements FileProcessor {

    private final searchAndEvalExpressions searchProcessor = new searchAndEvalExpressions(); // Используем готовый класс

    @Override
    public String process(String content) {
        try {
            // Парсим XML в дерево узлов
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(content.getBytes()));

            // Обрабатываем дерево узлов XML
            processXmlNode(document.getDocumentElement());

            // Преобразуем дерево обратно в строку
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));

            return writer.toString();
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка обработки XML: " + e.getMessage(), e);
        }
    }

    private void processXmlNode(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            // Рекурсивная обработка дочерних узлов
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                processXmlNode(children.item(i));
            }
        } else if (node.getNodeType() == Node.TEXT_NODE) {
            // Обработка текстового содержимого
            String text = node.getTextContent();
            String processedText = processText(text);
            node.setTextContent(processedText);
        }
    }

    private String processText(String text) {
        return searchProcessor.methodLib(text);
    }
}