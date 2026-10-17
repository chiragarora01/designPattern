# CountDownLatch — what it is and how we use it

## What it is

A `CountDownLatch(count)` is a **one-way gate**:

- `countDown()` decrements the count.
- `await()` blocks until the count reaches **zero**.
- It never resets. (Resettable version: `CyclicBarrier`.)

That's it. The entire tool is one number and two operations — which is why the "release 100 threads
at the same instant" pattern uses **two** latches.

```java
CountDownLatch latch = new CountDownLatch(3);
// thread A: latch.countDown()   // 3 -> 2
// thread B: latch.countDown()   // 2 -> 1
// thread C: latch.countDown()   // 1 -> 0  — everyone waiting in await() wakes at once
```

## The problem it solves

Testing a race (singleton init, ID generation) needs all threads to hit the target code at the same
instant. But thread starts are staggered by scheduler jitter — thread #100 might not even begin
while thread #1 has finished. A race test where the race never fires **passes vacuously**: it's a
fake test that proves nothing.

## The two-latch pattern

One latch converts "I started N threads" into "N threads are **verifiably waiting**". Then a second
latch fires the starting gun.

```java
int THREADS = 100;
CountDownLatch ready = new CountDownLatch(THREADS);  // every worker announces "I'm at the line"
CountDownLatch start = new CountDownLatch(1);        // the starting gun, held by main

// EACH WORKER:
ready.countDown();          // 1. "I'm set up and ready."  MUST be first — this is how main
                            //    knows all workers actually exist and are waiting
try {
    start.await();          // 2. freeze at the line until the gun fires
    doWork();               // 3. the thing under test — all threads released ~simultaneously
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();  // convention: restore the flag and bail,
}                                        // never swallow silently

// MAIN:
for (Thread t : threads) t.start();  // launch all 100 — each ends up waiting on `start`
ready.await();                       // block until all 100 counted down -> "everyone is at the line"
start.countDown();                   // fire: start 1 -> 0 -> ALL await() calls return at once
for (Thread t : threads) t.join();   // wait for every worker to FINISH before reading results
```

Why two and not one: with only `start`, main could fire the gun before some workers even started —
the "same instant" claim becomes a lie. The `ready` latch turns thread starts into a verifiable
count.

Why `join()` at the end: main must wait for every worker's `run()` to return before reading
results, otherwise it may assert against half-finished work.

## The free bonus: happens-before

`countDown()` → return from `await()` establishes a **happens-before** edge: everything a worker
wrote before counting down is visible to whoever wakes from `await()`. This is why plain arrays
and plain collections are safe to use for results — the latch guarantees visibility, so you don't
need concurrent structures for that reason alone. (You still need thread-safe structures when
multiple *workers* write to the same collection concurrently — e.g. `ConcurrentHashMap.newKeySet()`.)

## Variants worth knowing

| Tool | Difference from CountDownLatch |
|---|---|
| `CyclicBarrier` | Reusable — the gate resets; threads rendezvous repeatedly |
| `Phaser` | Like CyclicBarrier with a dynamic party count |
| `Semaphore` | N-way permit pool instead of a one-shot gate |

Rule of thumb: one-shot "wait until everyone is ready / wait until event happened" → CountDownLatch.

## The test checklist

A concurrency test that can't fail is not a test. Before running one, check:

1. **Can it detect the bug?** If the singleton weren't thread-safe, would this test catch it?
   (Latch gate: yes. Staggered thread starts: barely.)
2. **Does it assert the outcome?** Same instance (`==`), unique IDs, load count — not just "it printed".
3. **Does it join before asserting?** Otherwise you're reading half-finished state.
