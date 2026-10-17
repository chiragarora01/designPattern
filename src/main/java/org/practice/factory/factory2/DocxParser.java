package org.practice.factory.factory2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DocxParser implements DocumentParser {
    @Override
    public boolean canParse(byte[] header) {
        return header.length >= 4 && header[0] == 'P' && header[1] == 'K'
                && header[2] == 0x03 && header[3] == 0x04;
    }

    @Override
    public Document parse(Path file) throws IOException {
        try (ZipFile zip = new ZipFile(file.toFile())) {
            ZipEntry entry = zip.getEntry("word/document.xml");
            if (entry == null) {
                throw new IllegalArgumentException("Not a valid DOCX: " + file.getFileName());
            }
            String xml = new String(zip.getInputStream(entry).readAllBytes(), StandardCharsets.UTF_8);
            String text = xml.replace("</w:p>", "\n")
                    .replaceAll("<[^>]+>", "")
                    .replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">");
            return new Document(file.getFileName().toString(), text.trim());
        }
    }
}
