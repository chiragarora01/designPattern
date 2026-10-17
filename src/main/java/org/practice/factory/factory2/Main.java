package org.practice.factory.factory2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) throws Exception {
        Path dir = Files.createTempDirectory("parsers");
        ParserFactory factory = new ParserFactory()
                .register("pdf", new PdfParser())
                .register("docx", new DocxParser())
                .register("csv", new CsvParser());

        // 1. PDF bytes with a .csv extension -> sniffed as PDF, not CSV
        Path pdf = dir.resolve("report.csv");
        Files.write(pdf, "%PDF-1.4 fake pdf bytes".getBytes(StandardCharsets.US_ASCII));
        check("pdf sniff", factory.parse(pdf).getText().startsWith("PDF"));

        // 2. real DOCX with the right extension -> text pulled out of the zip
        Path docx = dir.resolve("letter.docx");
        writeDocx(docx, "Hello from inside the zip");
        check("docx parse", factory.parse(docx).getText().contains("Hello from inside"));

        // 3. plain text with a .pdf extension -> no magic matches, CSV text heuristic catches it
        Path csv = dir.resolve("data.pdf");
        Files.write(csv, "name,age\nAlice,30".getBytes(StandardCharsets.UTF_8));
        Document doc = factory.parse(csv);
        check("csv fallback", doc.getTitle().equals("name") && doc.getText().contains("Alice,30"));

        // 4. unknown type -> clear error, not null
        Path bin = dir.resolve("image.xyz");
        Files.write(bin, new byte[]{0, 1, 2, 3});
        try {
            factory.parse(bin);
            throw new AssertionError("expected failure");
        } catch (IllegalArgumentException e) {
            check("unknown type", e.getMessage().contains("image.xyz"));
        }

        System.out.println("all checks passed");
    }

    private static void writeDocx(Path path, String text) throws IOException {
        try (ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(path))) {
            zip.putNextEntry(new ZipEntry("word/document.xml"));
            String xml = "<?xml version=\"1.0\"?><w:document xmlns:w=\"w\"><w:p><w:t>"
                    + text + "</w:t></w:p></w:document>";
            zip.write(xml.getBytes(StandardCharsets.UTF_8));
        }
    }

    private static void check(String name, boolean ok) {
        if (!ok) {
            throw new AssertionError(name);
        }
        System.out.println("ok: " + name);
    }
}
