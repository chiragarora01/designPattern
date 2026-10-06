package org.practice.observer2;

import org.practice.observer2.service.Service;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Owns HOW a consumer is fed, so delivery policy is not the consumer's concern.
 * INLINE   -> delivered on the publisher thread (fast, latency-critical consumers).
 * ASYNC    -> own single thread + conflating buffer (slow consumers, e.g. UI).
 */
public class Subscription {

    public enum Mode {INLINE, ASYNC}

    private final Service service;
    private final PriceListener listener;
    private final Mode mode;

    // Bounded by design: one slot per currency pair. A newer quote overwrites the
    // older one for that pair (conflation) instead of queueing stale ticks.
    private final Map<CcyPairs, PriceEvent> pending = new ConcurrentHashMap<>();
    private final AtomicBoolean draining = new AtomicBoolean(false);
    private final ExecutorService executor;

    public Subscription(Service service, PriceListener listener, Mode mode) {
        this.service = service;
        this.listener = listener;
        this.mode = mode;
        // One dedicated thread per slow consumer: a shared pool would let one
        // slow consumer starve the others.
        this.executor = mode == Mode.ASYNC ? Executors.newSingleThreadExecutor() : null;
    }

    public void offer(PriceEvent event) {
        if (!service.isCcyPairActive(event.ccyPairs())) return;

        if (mode == Mode.INLINE) {
            listener.onPriceUpdate(event);
            return;
        }

        pending.put(event.ccyPairs(), event);       // conflate
        // Schedule one drain at a time; publisher never blocks here.
        if (draining.compareAndSet(false, true)) {
            executor.execute(this::drain);
        }
    }

    private void drain() {
        try {
            for (CcyPairs pair : pending.keySet()) {
                PriceEvent e = pending.remove(pair);
                if (e != null) listener.onPriceUpdate(e);
            }
        } finally {
            draining.set(false);
            // Something arrived while we were draining -> pick it up.
            if (!pending.isEmpty() && draining.compareAndSet(false, true)) {
                executor.execute(this::drain);
            }
        }
    }

    public void close() {
        if (executor != null) executor.shutdownNow();
    }
}
