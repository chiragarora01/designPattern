package org.practice.singleton.singleton1;

import java.util.concurrent.CountDownLatch;

public class Main {
    private static final int THREADS = 100;

    public static void main(String[] args) throws Exception {
        CountDownLatch ready = new CountDownLatch(THREADS);
        CountDownLatch start = new CountDownLatch(1);
        Config[] results = new Config[THREADS];
        Thread[] threads = new Thread[THREADS];

        for (int i = 0; i < THREADS; i++) {
            final int idx = i;
            threads[i] = new Thread(() -> {
                ready.countDown();
                try {
                    start.await();               // all 100 released at the same instant
                    results[idx] = singleton1.getConfig();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "t" + i);
            threads[i].start();
        }

        ready.await();                           // wait until all 100 are standing at the gate
        start.countDown();                       // go
        for (Thread t : threads) {
            t.join();
        }

        Config first = results[0];
        for (int i = 1; i < THREADS; i++) {
            if (results[i] != first) {
                throw new AssertionError("thread " + i + " got a different instance");
            }
        }
        if (Config.loadCount() != 1) {
            throw new AssertionError("Config constructed " + Config.loadCount() + " times");
        }
        System.out.println("ok: 100 threads -> all same instance, loaded exactly once");
        System.out.println("ok: readable from anywhere: app.name=" + first.get("app.name"));
    }
}
