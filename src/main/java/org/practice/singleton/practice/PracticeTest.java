package org.practice.singleton.practice;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * TASK: write the proof.
 * 1. THREADS threads released by TWO latches (ready + start), each calling activate()
 *    IDS_PER_THREAD times. Assert DISTINCT values == THREADS * IDS_PER_THREAD.
 * 2. Serialize a live LicensedService, deserialize it, assert == the live instance.  (TODO E)
 * 3. Reflection attack, assert it throws.                                            (TODO F)
 */
public class PracticeTest {
    private static final int THREADS = 50;
    private static final int IDS_PER_THREAD = 20;

    public static void main(String[] args) throws Exception {
        Set<Integer> results = ConcurrentHashMap.newKeySet();
        Thread[] threads = new Thread[THREADS];

        // two latches: `ready` proves the workers are verifiably waiting,
        // `start` is the gun that fires them all at once
        CountDownLatch ready = new CountDownLatch(THREADS);
        CountDownLatch start = new CountDownLatch(1);

        // worker: announce ready FIRST, then freeze at the line, then the loop
        for(int i=0;i<THREADS;i++){
            threads[i] = new Thread(()->
            {
                ready.countDown();
                try {
                    start.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                for(int j=0;j<IDS_PER_THREAD;j++){
                    results.add(LicensedService.getLicensedService().activate());
                }
            });
            threads[i].start();
        }

        // the gun fires only once everyone is standing at the line
        ready.await();
        start.countDown();

        // wait for every worker to finish before asserting
        for(int i=0;i<THREADS;i++){
            threads[i].join();
        }

        // failure must stop the run — a print can't fail
        if (results.size() != THREADS * IDS_PER_THREAD) {
            throw new AssertionError(
                    "expected " + (THREADS * IDS_PER_THREAD) + " distinct ids, got " + results.size());
        }
        System.out.println("ok: " + results.size() + " distinct ids");

        // TODO E: serialization round trip -> assert same object (==)

        // TODO F: reflection attack -> assert blocked

        System.out.println("all checks passed");
    }
}
