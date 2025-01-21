package com.example.server.processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionFileProcessorTest {

    private EncryptionFileProcessor encryptionFileProcessor;

    @BeforeEach
    void setUp() {
        encryptionFileProcessor = new EncryptionFileProcessor();
    }

    @Test
    void testProcessValidInput() {
        String input = "Hello, World!";

        String expectedEncryptedBase64 = "6w3H+IlXnYwkWRPm6hOUIA==";

        String result = encryptionFileProcessor.process(input);

        assertEquals(expectedEncryptedBase64, result, "Зашифрованное значение не совпадает с ожидаемым");
    }

    @Test
    void testProcessEmptyInput() {
        String input = "";

        String expectedEncryptedBase64 = "IhtXErS0+ZoF1hhWZSll8A==";

        String result = encryptionFileProcessor.process(input);

        assertEquals(expectedEncryptedBase64, result, "Зашифрованное значение пустой строки не совпадает с ожидаемым");
    }

    @Test
    void testProcessNullInput() {
        String input = null;

        assertThrows(NullPointerException.class, () -> encryptionFileProcessor.process(input),
                "Метод process должен выбрасывать исключение при null входных данных");
    }
}
