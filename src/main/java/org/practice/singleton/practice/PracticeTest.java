package org.practice.singleton.practice;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TASK: write the proof. Type the two-latch pattern yourself — that's the part you must own.
 *
 * 1. THREADS threads released by ONE CountDownLatch gate, each calling activate()
 *    IDS_PER_THREAD times. Assert the number of DISTINCT values == THREADS * IDS_PER_THREAD.
 * 2. Serialize a live LicensedService, deserialize it, assert == the live instance.
 * 3. Reflection attack (getDeclaredConstructor + setAccessible(true) + newInstance),
 *    assert it throws (the cause wraps your IllegalStateException).
 *
 * Latch reminders:
 *  - two latches: ready (count = THREADS) and start (count = 1)
 *  - worker: ready.countDown() FIRST, then start.await(), then the loop
 *  - main: launch all -> ready.await() -> start.countDown() -> join everyone
 *  - why two? one only proves "I started 100 threads", the pair proves
 *    "100 threads are verifiably waiting" before the gun fires
 */
public class PracticeTest {
    private static final int THREADS = 50;
    private static final int IDS_PER_THREAD = 20;

    public static void main(String[] args) throws Exception {
        Set<Integer> results = ConcurrentHashMap.newKeySet();

        // TODO A: declare the two latches

        // TODO B: spawn THREADS workers with the worker pattern above,
        //         each adding activate() results into `results`

        // TODO C: launch, wait at ready, fire start, join everyone

        // TODO D: assert results.size() == THREADS * IDS_PER_THREAD

        // TODO E: serialization round trip -> assert same object

        // TODO F: reflection attack -> assert blocked

        System.out.println("all checks passed");
    }
}
