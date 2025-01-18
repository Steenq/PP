package com.example.server.controller;

import com.example.server.factory.FileProcessorFactory;
import com.example.server.processor.EncryptionFileProcessor;
import com.example.server.processor.FileProcessor;
import com.example.server.unzipping.FileUnzip;
import com.example.server.unzipping.UnRar;
import com.example.server.unzipping.UnZip;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/files")
public class FileUploadController {
    private static final String AES = "AES";
    private static final String AES_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String FIXED_KEY = "1234567890123456";
    private static final String FIXED_IV = "abcdefghijklmnop";
    @PostMapping("/upload")
    public ResponseEntity<byte[]> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("archivingMethod") String archivingMethod,
            @RequestParam("encryptionMethod") String encryptionMethod,
            @RequestParam("isEncrypted") String isEncrypted) throws Exception {

        try {
            String FileName = file.getOriginalFilename().split("\\.")[0];
            String fileType = file.getOriginalFilename().split("\\.")[1];

            String content;
            if ("zip".equalsIgnoreCase(fileType)) {
                FileUnzip unzipping = new UnZip();
                UnZip UnZipProc = new UnZip();
                ImmutablePair<String, String> unzippedFile = UnZipProc.Unzipping(file.getBytes());
                content = unzippedFile.getRight();
                fileType = unzippedFile.getLeft(); // Обновляем тип файла
            } else if ("rar".equalsIgnoreCase(fileType)) {
                FileUnzip unzipping = new UnRar();
                UnRar UnRarProc = new UnRar();
                ImmutablePair<String, String> unzippedFile = UnRarProc.Unzipping(file.getBytes());
                content = unzippedFile.getRight();
                fileType = unzippedFile.getLeft(); // Обновляем тип файла
            }else {
                content = new String(file.getBytes(), StandardCharsets.UTF_8);
            }

            System.out.println(isEncrypted);
            if("on".equalsIgnoreCase(isEncrypted)) {
                content = decrypt(content, FIXED_KEY, FIXED_IV);
            }
            FileProcessor processor = FileProcessorFactory.getProcessor(fileType);

            String processedContent = processor.process(content);

            byte[] processedFileBytes;

            if("aes".equalsIgnoreCase(encryptionMethod)) {
                FileProcessor encryption = new EncryptionFileProcessor();
                processedContent = encryption.process(processedContent);
            }
            if ("zip".equalsIgnoreCase(archivingMethod)) {
                processedFileBytes = archiveAsZip(processedContent, FileName, fileType);
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Disposition", "attachment; filename=" + FileName + "_processed.zip");

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .headers(headers)
                        .body(processedFileBytes);
            } else if ("rar".equalsIgnoreCase(archivingMethod)) {
                processedFileBytes = archiveAsRar(processedContent);
            } else {
                processedFileBytes = processedContent.getBytes(StandardCharsets.UTF_8);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=" + FileName + "_processed." + fileType);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .headers(headers)
                    .body(processedFileBytes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(("Ошибка: " + e.getMessage()).getBytes());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(("Ошибка чтения файла").getBytes());
        }
    }

    private byte[] archiveAsZip(String content, String Filename, String fileType) {

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {

            ZipEntry zipEntry = new ZipEntry("processed_" + Filename + "." + fileType);
            zipOutputStream.putNextEntry(zipEntry);

            zipOutputStream.write(content.getBytes(StandardCharsets.UTF_8));
            zipOutputStream.closeEntry();

            zipOutputStream.finish();

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при создании ZIP-архива: " + e.getMessage(), e);
        }
    }

    private byte[] archiveAsRar(String content) {

        return content.getBytes(StandardCharsets.UTF_8);
    }
    // Метод для расшифровки
    private String decrypt(String encryptedData, String key, String iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), AES);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }

}
