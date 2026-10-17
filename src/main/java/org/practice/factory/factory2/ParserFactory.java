package org.practice.factory.factory2;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class ParserFactory {
    private final Map<String, DocumentParser> parsers = new LinkedHashMap<>();

    public ParserFactory register(String extension, DocumentParser parser) {
        parsers.put(extension.toLowerCase(), parser);
        return this;
    }

    public DocumentParser getParser(Path file) throws IOException {
        byte[] header = readHeader(file);
        // magic bytes first (registration order), extension as fallback
        for (DocumentParser parser : parsers.values()) {
            if (parser.canParse(header)) {
                return parser;
            }
        }
        DocumentParser parser = parsers.get(extensionOf(file));
        if (parser == null) {
            throw new IllegalArgumentException("Unsupported file type: " + file.getFileName());
        }
        return parser;
    }

    public Document parse(Path file) throws IOException {
        return getParser(file).parse(file);
    }

    private static byte[] readHeader(Path file) throws IOException {
        try (InputStream in = Files.newInputStream(file)) {
            byte[] buf = new byte[512];
            int n = in.read(buf);
            return n <= 0 ? new byte[0] : Arrays.copyOf(buf, n);
        }
    }

    private static String extensionOf(Path file) {
        String name = file.getFileName().toString();
        int dot = name.lastIndexOf('.');
        return dot < 0 ? "" : name.substring(dot + 1).toLowerCase();
    }
}
