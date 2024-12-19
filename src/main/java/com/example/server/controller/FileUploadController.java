package com.example.server.controller;

import com.example.server.factory.FileProcessorFactory;
import com.example.server.processor.FileProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/files")
public class FileUploadController {

    @PostMapping("/upload")
    public ResponseEntity<byte[]> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("archivingMethod") String archivingMethod) {

        try {
            String FileName = file.getOriginalFilename().split("\\.")[0];
            String fileType = file.getOriginalFilename().split("\\.")[1];

            String content = new String(file.getBytes(), StandardCharsets.UTF_8);

            FileProcessor processor = FileProcessorFactory.getProcessor(fileType);

            String processedContent = processor.process(content);

            byte[] processedFileBytes;

            // Archiving logic based on user's choice
            if ("zip".equalsIgnoreCase(archivingMethod)) {
                processedFileBytes = archiveAsZip(processedContent);
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

    private byte[] archiveAsZip(String content) {

        return content.getBytes(StandardCharsets.UTF_8);
    }

    private byte[] archiveAsRar(String content) {

        return content.getBytes(StandardCharsets.UTF_8);
    }
}
