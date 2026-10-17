package org.practice.singleton.practice;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TASK: make this a singleton that survives BOTH serialization and reflection.
 * Type every TODO yourself. Don't open singleton2 until you're stuck.
 *
 * Recall the mental model: serialization is a constructor-free copy machine;
 * readResolve is your one chance to say "throw that copy away, use this instead".
 */
public class LicensedService implements Serializable {

    // TODO 2: the version stamp field. What breaks on readObject if you
    //         add a field later and never declared this?

    // TODO 3: the one instance. Which keyword makes double-checked locking
    //         safe, and what specifically does it prevent? (Say it out loud before typing.)

    // state this singleton protects
    private final AtomicInteger activations = new AtomicInteger();

    // TODO 4: private constructor + the reflection guard.
    //         HINT: serialization NEVER runs this method — reflection ALWAYS does.
    //         Why does the guard pass on the FIRST legit call but block every later one?

    // TODO 5: public accessor with correct double-checked locking:
    //         outer check -> lock -> inner check -> assign.
    //         Which two things is the inner check protecting against?

    // TODO 6: the serialization hook. Exact signature? Why private?
    //         Why return the accessor instead of the field directly?

    public int activate() {
        return activations.incrementAndGet();
    }

    public int getActivations() {
        return activations.get();
    }
}
