package org.practice.observer.observer3;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Admin {

    ScheduledExecutorService executorService;
    EventNotifier eventNotifier;
    AtomicInteger atomicInteger = new AtomicInteger(0);

    public Admin(EventNotifier eventNotifier) {
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.eventNotifier = eventNotifier;
    }

    public void stop() {
        executorService.shutdownNow();
    }

    public void start() {
        executorService.scheduleAtFixedRate(
                () -> eventNotifier.notifyOnVideoUpload("Video " + atomicInteger.incrementAndGet()),
                0,
                100,
                TimeUnit.MILLISECONDS
        );
    }
}
