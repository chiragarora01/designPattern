package org.practice.factory.factory4;

import java.time.LocalDate;

public class Message {

    private double price;
    private SecurityType securityType;
    private double amount;
    private double fwdPts;
    private LocalDate fwdDate;
    private double farPts;
    private LocalDate farDate;
    private LocalDate expiryDate;

    private Message(Builder builder) {
        this.price = builder.price;
        this.securityType = builder.securityType;
        this.amount = builder.amount;
        this.fwdPts = builder.fwdPts;
        this.fwdDate = builder.fwdDate;
        this.farPts = builder.farPts;
        this.farDate = builder.farDate;
        this.expiryDate = builder.expiryDate;
    }

    public double getPrice() {
        return price;
    }

    public SecurityType getSecurityType() {
        return securityType;
    }

    public double getAmount() {
        return amount;
    }

    public double getFwdPts() {
        return fwdPts;
    }

    public LocalDate getFwdDate() {
        return fwdDate;
    }

    public double getFarPts() {
        return farPts;
    }

    public LocalDate getFarDate() {
        return farDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public static class Builder {
        private double price;
        private SecurityType securityType;
        private double amount;
        private double fwdPts;
        private LocalDate fwdDate;
        private double farPts;
        private LocalDate farDate;
        private LocalDate expiryDate;

        public Builder price(double price) {
            this.price = price;
            return this;
        }

        public Builder securityType(SecurityType securityType) {
            this.securityType = securityType;
            return this;
        }

        public Builder amount(double amount) {
            this.amount = amount;
            return this;
        }

        public Builder fwdPts(double fwdPts) {
            this.fwdPts = fwdPts;
            return this;
        }

        public Builder fwdDate(LocalDate fwdDate) {
            this.fwdDate = fwdDate;
            return this;
        }

        public Builder farPts(double farPts) {
            this.farPts = farPts;
            return this;
        }

        public Builder farDate(LocalDate farDate) {
            this.farDate = farDate;
            return this;
        }

        public Builder expiryDate(LocalDate expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }
}