package com.example.server.processor;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionFileProcessor implements FileProcessor {

    private static final String AES = "AES";
    private static final String AES_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    private static final String FIXED_KEY = "1234567890123456";

    private static final String FIXED_IV = "abcdefghijklmnop";

    @Override
    public String process(String content) {
        try {
            byte[] encryptedContent = encrypt(content, FIXED_KEY, FIXED_IV);
            // Кодируем результат в Base64 для удобства хранения и передачи
            return Base64.getEncoder().encodeToString(encryptedContent);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка шифрования: " + e.getMessage(), e);
        }
    }

    // Метод шифрования с фиксированным ключом и IV
    private byte[] encrypt(String data, String key, String iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), AES);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        return cipher.doFinal(data.getBytes());
    }
}
