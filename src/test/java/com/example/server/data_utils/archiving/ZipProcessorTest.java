package com.example.server.data_utils.archiving;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.*;

class ZipProcessorTest {

    private ZipProcessor zipProcessor;

    @BeforeEach
    void setUp() {
        zipProcessor = new ZipProcessor();
    }

    @Test
    void testArchiveValidContent() throws IOException {
        String content = "This is a test content";
        String filename = "testfile";
        String fileType = "txt";

        byte[] zipData = zipProcessor.archive(content, filename, fileType);

        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(zipData))) {
            ZipEntry entry = zipInputStream.getNextEntry();
            assertNotNull(entry, "ZIP-архив должен содержать файл");
            assertEquals("processed_testfile.txt", entry.getName(), "Имя файла в архиве не совпадает с ожидаемым");

            byte[] buffer = zipInputStream.readAllBytes();
            String extractedContent = new String(buffer);
            assertEquals(content, extractedContent, "Содержимое файла в архиве не совпадает с ожидаемым");
        }
    }

    @Test
    void testArchiveEmptyContent() throws IOException {
        String content = "";
        String filename = "emptyfile";
        String fileType = "txt";

        byte[] zipData = zipProcessor.archive(content, filename, fileType);

        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(zipData))) {
            ZipEntry entry = zipInputStream.getNextEntry();
            assertNotNull(entry, "ZIP-архив должен содержать файл");
            assertEquals("processed_emptyfile.txt", entry.getName(), "Имя файла в архиве не совпадает с ожидаемым");

            byte[] buffer = zipInputStream.readAllBytes();
            String extractedContent = new String(buffer);
            assertEquals(content, extractedContent, "Содержимое файла в архиве не совпадает с ожидаемым");
        }
    }

    @Test
    void testArchiveWithNullContent() {
        String content = null;
        String filename = "nullfile";
        String fileType = "txt";

        assertThrows(NullPointerException.class, () -> zipProcessor.archive(content, filename, fileType),
                "Метод должен выбросить исключение при null содержимом");
    }

    @Test
    void testArchiveWithLongFilename() throws IOException {
        String content = "Content for long filename test";
        String filename = "this_is_a_very_long_filename_to_test_the_functionality_of_the_zip_processor_class";
        String fileType = "log";

        byte[] zipData = zipProcessor.archive(content, filename, fileType);

        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(zipData))) {
            ZipEntry entry = zipInputStream.getNextEntry();
            assertNotNull(entry, "ZIP-архив должен содержать файл");
            assertEquals("processed_" + filename + ".log", entry.getName(), "Имя файла в архиве не совпадает с ожидаемым");

            byte[] buffer = zipInputStream.readAllBytes();
            String extractedContent = new String(buffer);
            assertEquals(content, extractedContent, "Содержимое файла в архиве не совпадает с ожидаемым");
        }
    }
}
