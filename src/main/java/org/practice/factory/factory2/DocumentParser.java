package org.practice.factory.factory2;

import java.io.IOException;
import java.nio.file.Path;

public interface DocumentParser {
    /** Sniffs the file's first bytes; true if this parser recognises the real type. */
    boolean canParse(byte[] header);

    Document parse(Path file) throws IOException;
}
