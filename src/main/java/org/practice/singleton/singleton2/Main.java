package org.practice.singleton.singleton2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class Main {
    private static final int THREADS = 100;
    private static final int IDS_PER_THREAD = 100;

    public static void main(String[] args) throws Exception {
        // 1. heavy concurrent load: every id unique
        CountDownLatch start = new CountDownLatch(1);
        Set<Integer> ids = ConcurrentHashMap.newKeySet();
        Thread[] threads = new Thread[THREADS];
        for (int i = 0; i < THREADS; i++) {
            threads[i] = new Thread(() -> {
                try {
                    start.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                for (int j = 0; j < IDS_PER_THREAD; j++) {
                    ids.add(IdGenerator.getIdGenerator().getId());
                }
            });
            threads[i].start();
        }
        start.countDown();
        for (Thread t : threads) {
            t.join();
        }
        check("10_000 unique ids", ids.size() == THREADS * IDS_PER_THREAD);

        // 2. reflection attack
        Constructor<IdGenerator> ctor = IdGenerator.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        try {
            ctor.newInstance();
            throw new AssertionError("reflection created a second instance");
        } catch (Exception e) {
            check("reflection blocked", e.getCause() instanceof IllegalStateException);
        }

        // 3. serialization round-trip
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(IdGenerator.getIdGenerator());
        }
        IdGenerator deserialized;
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()))) {
            deserialized = (IdGenerator) in.readObject();
        }
        check("serialization keeps one instance", deserialized == IdGenerator.getIdGenerator());

        System.out.println("all checks passed");
    }

    private static void check(String name, boolean ok) {
        if (!ok) {
            throw new AssertionError(name);
        }
        System.out.println("ok: " + name);
    }
}
