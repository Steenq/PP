package com.example.server.data_utils.dearchiving;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class UnZipTest {

    private UnZip unZip;

    @BeforeEach
    void setUp() {
        unZip = new UnZip();
    }

    @Test
    void testValidZipFile() throws Exception {
        String fileName = "testfile.txt";
        String fileContent = "Hello, World!";
        byte[] zipContent = createZipFile(fileName, fileContent);

        String expectedFileType = "txt";
        String expectedContent = fileContent;
        ImmutablePair<String, String> result = unZip.Unzipping(zipContent);

        assertEquals(expectedFileType, result.getLeft(), "Тип файла не совпадает с ожидаемым");
        assertEquals(expectedContent, result.getRight(), "Содержимое файла не совпадает с ожидаемым");
    }

    @Test
    void testEmptyZipFile() {
        byte[] zipContent = new byte[0];
        assertThrows(IllegalArgumentException.class, () -> unZip.Unzipping(zipContent),
                "Ожидалось исключение для пустого архива");
    }

    private byte[] createZipFile(String fileName, String fileContent) throws Exception {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {

            zipOutputStream.putNextEntry(new ZipEntry(fileName));
            zipOutputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
            zipOutputStream.closeEntry();

            zipOutputStream.finish();
            return byteArrayOutputStream.toByteArray();
        }
    }
}
