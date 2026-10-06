package org.practice.observer2.service;

import org.practice.observer2.CcyPairs;
import org.practice.observer2.PriceEvent;
import org.practice.observer2.PriceListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PriceEngine implements Service, PriceListener {
    String name;
    List<CcyPairs> ccyPairs = new CopyOnWriteArrayList<>();

    public PriceEngine(String name) {
        this.name = name;
    }

    @Override
    public void onPriceUpdate(PriceEvent priceEvent) {
        System.out.println("Price reached price engine " + priceEvent);
    }

    @Override
    public void enableCurrencyPair(CcyPairs ccyPair){
        ccyPairs.add(ccyPair);
    }

    @Override
    public void disableCurrencyPair(CcyPairs ccyPair){
        ccyPairs.remove(ccyPair);
    }


    @Override
    public boolean isCcyPairActive(CcyPairs ccyPair) {
        return ccyPairs.contains(ccyPair);
    }
}

