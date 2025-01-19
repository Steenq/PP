package com.example.server.service.dearchiving;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UnRar implements FileUnzip {
    @Override
    public ImmutablePair<String, String> Unzipping(byte[] content) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content);
             Archive archive = new Archive(byteArrayInputStream)) {

            if (archive.isEncrypted()) {
                throw new IllegalArgumentException("RAR-архив зашифрован и не может быть обработан");
            }

            FileHeader fileHeader = archive.nextFileHeader();
            if (fileHeader == null) {
                throw new IllegalArgumentException("RAR-архив пустой или поврежден");
            }

            // Читаем содержимое файла
            String fileName = fileHeader.getFileNameString();
            String fileType = fileName.substring(fileName.lastIndexOf('.') + 1);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            archive.extractFile(fileHeader, outputStream);
            String fileContent = outputStream.toString("UTF-8");

            return ImmutablePair.of(fileType, fileContent);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при разархивировании RAR: " + e.getMessage(), e);
        } catch (RarException e) {
            throw new RuntimeException(e);
        }
    }
}
