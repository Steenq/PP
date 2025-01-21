package com.example.server.data_utils.decryption;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class decryptionAes16Test {

    private decryptionAes16 decryptionAes16;

    @BeforeEach
    void setUp() {
        decryptionAes16 = new decryptionAes16();
    }

    @Test
    void testDecryptValidData() throws Exception {
        // Данные, соответствующие зашифрованному "Hello, World!" с фиксированным ключом и IV
        String encryptedData = "6w3H+IlXnYwkWRPm6hOUIA=="; // Предварительно зашифрованное значение
        String expectedDecrypted = "Hello, World!";

        String result = decryptionAes16.decrypt(encryptedData, "1234567890123456", "abcdefghijklmnop");
        assertEquals(expectedDecrypted, result, "Расшифрованное значение не совпадает с ожидаемым");
    }

    @Test
    void testDecryptWithEmptyData() throws Exception {
        String encryptedData = "IhtXErS0+ZoF1hhWZSll8A=="; // Зашифрованная пустая строка
        String expectedDecrypted = "";

        String result = decryptionAes16.decrypt(encryptedData, "1234567890123456", "abcdefghijklmnop");
        assertEquals(expectedDecrypted, result, "Расшифрованное значение пустой строки не совпадает с ожидаемым");
    }

    @Test
    void testDecryptWithIncorrectKey() {
        String encryptedData = "oHVYOq6ZzS5XKeX8iwD62A=="; // Зашифрованное значение
        String incorrectKey = "wrongkey12345678"; // Некорректный ключ

        assertThrows(Exception.class, () ->
                        decryptionAes16.decrypt(encryptedData, incorrectKey, "abcdefghijklmnop"),
                "Метод должен выбрасывать исключение при неверном ключе"
        );
    }

    @Test
    void testDecryptWithIncorrectIV() {
        String encryptedData = "oHVYOq6ZzS5XKeX8iwD62A=="; // Зашифрованное значение
        String incorrectIV = "wrongiv123456789"; // Некорректный IV

        assertThrows(Exception.class, () ->
                        decryptionAes16.decrypt(encryptedData, "1234567890123456", incorrectIV),
                "Метод должен выбрасывать исключение при неверном IV"
        );
    }
}
