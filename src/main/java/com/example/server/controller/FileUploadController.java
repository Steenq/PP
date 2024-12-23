package com.example.server.controller;

import com.example.server.factory.FileProcessorFactory;
import com.example.server.processor.FileProcessor;
import com.example.server.unzipping.FileUnzip;
import com.example.server.unzipping.UnZip;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

            String content;
            if ("zip".equalsIgnoreCase(fileType)) {
                FileUnzip unzipping = new UnZip();
                UnZip UnZipProc = new UnZip();
                ImmutablePair<String, String> unzippedFile = UnZipProc.Unzipping(file.getBytes());
                content = unzippedFile.getRight();
                fileType = unzippedFile.getLeft(); // Обновляем тип файла
            } else {
                content = new String(file.getBytes(), StandardCharsets.UTF_8);
            }

            FileProcessor processor = FileProcessorFactory.getProcessor(fileType);

            String processedContent = processor.process(content);

            byte[] processedFileBytes;

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
}
