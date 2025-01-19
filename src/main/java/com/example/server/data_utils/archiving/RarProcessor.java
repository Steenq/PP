package com.example.server.data_utils.archiving;

import java.nio.charset.StandardCharsets;

public class RarProcessor implements archivingProcess {
    @Override
    public byte[] archive(String content, String Filename, String fileType) {

        return content.getBytes(StandardCharsets.UTF_8);
    }
}
