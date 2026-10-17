package org.practice.observer.observer2.service;

import org.practice.observer.observer2.CcyPairs;
import org.practice.observer.observer2.PriceEvent;
import org.practice.observer.observer2.PriceListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UI implements Service, PriceListener {
    String name;
    List<CcyPairs> ccyPairs = new CopyOnWriteArrayList<>();


    public UI(String name) {
        this.name = name;
    }

    @Override
    public void onPriceUpdate(PriceEvent priceEvent) {
        try {
            Thread.sleep(200); // simulate the slow consumer
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        System.out.println("Price reached ui " + priceEvent);
    }

    @Override
    public boolean isCcyPairActive(CcyPairs ccyPair) {
        return ccyPairs.contains(ccyPair);
    }

    @Override
    public void enableCurrencyPair(CcyPairs ccyPair) {
        ccyPairs.add(ccyPair);
    }

    @Override
    public void disableCurrencyPair(CcyPairs ccyPair) {
        ccyPairs.remove(ccyPair);
    }

}

