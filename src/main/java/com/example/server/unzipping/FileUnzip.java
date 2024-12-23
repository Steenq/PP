package com.example.server.unzipping;

import ch.qos.logback.core.joran.sanity.Pair;
import org.apache.commons.lang3.tuple.ImmutablePair;

public interface FileUnzip {
    public ImmutablePair<String, String> Unzipping(byte[] content);
}
