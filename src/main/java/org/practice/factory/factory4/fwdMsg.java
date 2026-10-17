package org.practice.factory.factory4;

import java.time.LocalDate;

public class fwdMsg extends spotMessage {

    private final double fwdPts;
    private final LocalDate fwdDate;

    public fwdMsg(Message message) {
        super(message);
        if (message.getFwdDate() == null) {
            throw new IllegalArgumentException("Forward requires fwdDate");
        }
        this.fwdPts = message.getFwdPts();
        this.fwdDate = message.getFwdDate();
    }

    public double getFwdPts() {
        return fwdPts;
    }

    public LocalDate getFwdDate() {
        return fwdDate;
    }
}
