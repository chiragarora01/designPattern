package org.practice.factory.factory4;

import java.time.LocalDate;

public class swpMsg extends fwdMsg {

    private final double farPts;
    private final LocalDate farDate;

    public swpMsg(Message message) {
        super(message);
        if (message.getFarDate() == null) {
            throw new IllegalArgumentException("Swap requires farDate");
        }
        this.farPts = message.getFarPts();
        this.farDate = message.getFarDate();
    }

    public double getFarPts() {
        return farPts;
    }

    public LocalDate getFarDate() {
        return farDate;
    }
}
