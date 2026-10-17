package org.practice.observer.observer2;

import java.util.List;
import java.util.concurrent.*;

public class PriceFeeder {

    private final ScheduledExecutorService scheduledExecutorService;
    private final EventDispatcher eventDispatcher;

    //    private final EventDis
    public PriceFeeder(List<CcyPairs> ccyPairsList, EventDispatcher eventDispatcher) {
        if (ccyPairsList.isEmpty())
            throw new RuntimeException();
        this.scheduledExecutorService = Executors.newScheduledThreadPool(ccyPairsList.size());
        this.eventDispatcher = eventDispatcher;
    }

    public void start() {
        scheduledExecutorService.scheduleAtFixedRate(
                () -> publish(CcyPairs.EURUSD),
                0,
                100,
                TimeUnit.MILLISECONDS
        );

        scheduledExecutorService.scheduleAtFixedRate(
                () -> publish(CcyPairs.EURJPY),
                0,
                100,
                TimeUnit.MILLISECONDS
        );

        scheduledExecutorService.scheduleAtFixedRate(
                () -> publish(CcyPairs.USDCAD),
                0,
                100,
                TimeUnit.MILLISECONDS
        );
    }

    private void publish(CcyPairs ccyPairs) {
        eventDispatcher.publish(new PriceEvent(ccyPairs, generatePrice()));

    }

    public void stop() {
        scheduledExecutorService.shutdownNow();
    }

    private double generatePrice() {
        return 100 + Math.random() * 10;
    }

}
