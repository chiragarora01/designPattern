package org.practice.factory.factory2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class PdfParser implements DocumentParser {
    @Override
    public boolean canParse(byte[] header) {
        return header.length >= 4
                && "%PDF".equals(new String(header, 0, 4, StandardCharsets.US_ASCII));
    }

    @Override
    public Document parse(Path file) throws IOException {
        // ponytail: stub text extraction — real PDFs compress their text; use PDFBox when that matters
        return new Document(file.getFileName().toString(), "PDF content (extraction needs PDFBox)");
    }
}
