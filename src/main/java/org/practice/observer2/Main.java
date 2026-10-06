package org.practice.observer2;

import org.practice.observer2.service.PriceEngine;
import org.practice.observer2.service.Risk;
import org.practice.observer2.service.Service;
import org.practice.observer2.service.UI;

import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        EventDispatcher eventDispatcher = new EventDispatcher();
        PriceFeeder priceFeeder = new PriceFeeder(List.of(CcyPairs.EURJPY, CcyPairs.EURUSD, CcyPairs.USDCAD), eventDispatcher);
        priceFeeder.start();
        Service service = new PriceEngine("priceEngine");
        Service ui = new UI("UI");
        Service risk = new Risk("risk");

        service.enableCurrencyPair(CcyPairs.EURUSD);
        service.enableCurrencyPair(CcyPairs.EURJPY);
        ui.enableCurrencyPair(CcyPairs.EURUSD);
        ui.enableCurrencyPair(CcyPairs.EURJPY);
        risk.enableCurrencyPair(CcyPairs.EURJPY);
        risk.enableCurrencyPair(CcyPairs.EURUSD);
        risk.enableCurrencyPair(CcyPairs.USDCAD);

        PriceListener serviceListener = (PriceListener) service;
        PriceListener uiListener = (PriceListener) ui;
        PriceListener riskListener = (PriceListener) risk;

        // Pricing + risk are latency-critical -> fed on the publisher thread.
        eventDispatcher.subscribe(service, serviceListener, Subscription.Mode.INLINE);
        eventDispatcher.subscribe(risk, riskListener, Subscription.Mode.INLINE);
        // UI is slow -> own thread, conflating buffer, cannot delay the others.
        eventDispatcher.subscribe(ui, uiListener, Subscription.Mode.ASYNC);

        risk.disableCurrencyPair(CcyPairs.EURUSD);

        Thread.sleep(2000);

        priceFeeder.stop();
        eventDispatcher.close();
    }
}
