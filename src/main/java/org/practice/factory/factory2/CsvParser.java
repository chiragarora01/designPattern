package org.practice.factory.factory2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvParser implements DocumentParser {
    // ponytail: "text without NUL bytes" is a weak heuristic — CSV has no magic bytes;
    // swap for content detection (Apache Tika) if precision matters
    @Override
    public boolean canParse(byte[] header) {
        if (header.length == 0) {
            return false;
        }
        for (byte b : header) {
            if (b == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Document parse(Path file) throws IOException {
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        String title = lines.isEmpty() ? file.getFileName().toString() : lines.get(0).split(",")[0];
        return new Document(title, String.join("\n", lines));
    }
}
