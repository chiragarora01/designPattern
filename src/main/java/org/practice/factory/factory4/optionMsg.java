package org.practice.factory.factory4;

import java.time.LocalDate;

public class optionMsg extends spotMessage {

    private final LocalDate expiryDate;

    public optionMsg(Message message) {
        super(message);
        if (message.getExpiryDate() == null) {
            throw new IllegalArgumentException("Option requires expiryDate");
        }
        this.expiryDate = message.getExpiryDate();
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }
}
