package com.example.server.controller;

import com.example.server.factory.FileProcessorFactory;
import com.example.server.processor.EncryptionFileProcessor;
import com.example.server.processor.FileProcessor;
import com.example.server.service.archiving.RarProcessor;
import com.example.server.service.archiving.ZipProcessor;
import com.example.server.service.archiving.archivingProcess;
import com.example.server.service.dearchiving.FileUnzip;
import com.example.server.service.dearchiving.UnRar;
import com.example.server.service.dearchiving.UnZip;
import com.example.server.service.decryption.decryptionAes16;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/files")
public class FileUploadController {
    private static final String FIXED_KEY = "1234567890123456";
    private static final String FIXED_IV = "abcdefghijklmnop";
    @PostMapping("/upload")
    public ResponseEntity<byte[]> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("archivingMethod") String archivingMethod,
            @RequestParam("encryptionMethod") String encryptionMethod,
            @RequestParam("isEncrypted") String isEncrypted) {

        try {
            String FileName = file.getOriginalFilename().split("\\.")[0];
            String fileType = file.getOriginalFilename().split("\\.")[1];
            System.out.println(fileType);
            String content;
            if ("zip".equalsIgnoreCase(fileType)) {
                FileUnzip unzipping = new UnZip();
                UnZip UnZipProc = new UnZip();
                ImmutablePair<String, String> unzippedFile = UnZipProc.Unzipping(file.getBytes());
                content = unzippedFile.getRight();
                fileType = unzippedFile.getLeft(); // Обновляем тип файла
                System.out.println(fileType);
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
            System.out.println(isEncrypted);
            if("off,on".equalsIgnoreCase(isEncrypted)) {
                decryptionAes16 dec = new decryptionAes16();
                content = dec.decrypt(content, FIXED_KEY, FIXED_IV);
            }
            FileProcessor processor = FileProcessorFactory.getProcessor(fileType);

            String processedContent = processor.process(content);

            byte[] processedFileBytes;

            if("aes".equalsIgnoreCase(encryptionMethod)) {
                FileProcessor encryption = new EncryptionFileProcessor();
                processedContent = encryption.process(processedContent);
            }
            if ("zip".equalsIgnoreCase(archivingMethod)) {
                archivingProcess proc1 = new ZipProcessor();
                processedFileBytes = proc1.archive(processedContent, FileName, fileType);
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Disposition", "attachment; filename=" + FileName + "_processed.zip");

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .headers(headers)
                        .body(processedFileBytes);
            } else if ("rar".equalsIgnoreCase(archivingMethod)) {
                archivingProcess proc2 = new RarProcessor();
                processedFileBytes = proc2.archive(processedContent, FileName, fileType);
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
