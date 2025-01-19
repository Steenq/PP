package com.example.server.service.dearchiving;

import org.apache.commons.lang3.tuple.ImmutablePair;

public interface FileUnzip {
    public ImmutablePair<String, String> Unzipping(byte[] content);
}
