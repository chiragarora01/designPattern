package org.practice.observer.observer2.service;

import org.practice.observer.observer2.CcyPairs;

public interface Service {

    void enableCurrencyPair(CcyPairs ccyPair);

    void disableCurrencyPair(CcyPairs ccyPair);

    boolean isCcyPairActive(CcyPairs ccyPairs);
}
