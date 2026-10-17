package org.practice.factory.factory4;

/** One per exchange: maps that exchange's wire format to the common Message. */
public interface ExchangeAdapter {
    Message translate();
}
