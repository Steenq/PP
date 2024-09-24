package com.xml;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXExample {
    final String fileName = "C:\\Users\\Steenq\\IdeaProjects\\untitled3\\src\\com\\xml\\phonebook.xml";

    DefaultHandler handler = new DefaultHandler() {
        boolean isValue = false; // флаг начала считывания значения тега

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            // Выводим начало тега
            System.out.println("<" + qName + ">");

            // Выводим атрибуты, если они есть
            for (int i = 0; i < attributes.getLength(); i++) {
                String attrName = attributes.getQName(i);
                String attrValue = attributes.getValue(i);
                System.out.println("Attribute: " + attrName + " = " + attrValue);
            }

            // Устанавливаем флаг для чтения текста внутри тега
            isValue = true;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            // Проверяем флаг и выводим текст между тегами
            if (isValue) {
                String value = new String(ch, start, length).trim();
                if (!value.isEmpty()) {
                    System.out.println(value);
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            // Закрытие тега
            System.out.println("</" + qName + ">");
            isValue = false;  // Сбрасываем флаг
        }

        @Override
        public void startDocument() throws SAXException {
            System.out.println("Начало разбора документа!");
        }

        @Override
        public void endDocument() throws SAXException {
            System.out.println("Разбор документа завершен!");
        }
    };

    public SAXExample() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(fileName, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new SAXExample();
    }
}
