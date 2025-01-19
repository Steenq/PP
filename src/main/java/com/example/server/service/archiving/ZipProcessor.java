package com.example.server.service.archiving;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipProcessor implements archivingProcess{
    @Override
    public byte[] archive(String content, String Filename, String fileType) {
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
}
