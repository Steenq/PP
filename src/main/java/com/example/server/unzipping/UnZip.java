package com.example.server.unzipping;

import ch.qos.logback.core.joran.sanity.Pair;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnZip implements FileUnzip{
    public ImmutablePair<String, String> Unzipping(byte[] content) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content);
             ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            ZipEntry entry = zipInputStream.getNextEntry();
            if (entry == null) {
                throw new IllegalArgumentException("ZIP-архив пустой или поврежден");
            }

            // Читаем содержимое текущего файла в архиве
            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = zipInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            zipInputStream.closeEntry();

            String fileName = entry.getName(); // Имя файла в архиве
            String fileType = fileName.substring(fileName.lastIndexOf('.') + 1); // Получаем расширение
            String fileContent = outputStream.toString(StandardCharsets.UTF_8);

            return ImmutablePair.of(fileType, fileContent);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при разархивировании: " + e.getMessage(), e);
        }
    }
}
