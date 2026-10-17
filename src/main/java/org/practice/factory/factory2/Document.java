package org.practice.factory.factory2;

public class Document {
    private final String title;
    private final String text;

    public Document(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
