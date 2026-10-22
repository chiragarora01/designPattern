package org.practice.singleton.practice;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TASK: make this a singleton that survives BOTH serialization and reflection.
 *
 * Recall the mental model: serialization is a constructor-free copy machine;
 * readResolve is your one chance to say "throw that copy away, use this instead".
 *
 * DONE: serialVersionUID, volatile instance, DCL accessor, reflection guard.
 * LEFT: TODO 6 (readResolve).
 */
public class LicensedService implements Serializable {

    private static final long serialVersionUID = 2L;

    private static volatile LicensedService licensedService;

    // state this singleton protects
    private final AtomicInteger activations = new AtomicInteger();

    // reflection guard: the constructor runs BEFORE the instance is assigned on the
    // happy path, so this passes exactly once — every later construction throws.
    // (Serialization never runs this method; that hole is closed by readResolve.)

    private LicensedService() {
        if (licensedService != null) {
            throw new IllegalStateException("Use getIdGenerator()");
        }
    }

    public static LicensedService getLicensedService() {
        if (licensedService == null) {
            synchronized (LicensedService.class) {
                if (licensedService == null) {
                    licensedService = new LicensedService();
                }
            }
        }
        return licensedService;
    }

    // TODO 6 (FIX): getLicensedServiceSerial is never called by the JVM —
    //               the hook is matched by EXACT signature: private Object readResolve().
    //               Rename it and make it private.
    //               Why return the accessor instead of the field directly?

    public Object getLicensedServiceSerial() {
        return getLicensedService();
    }

    public int activate() {
        return activations.incrementAndGet();
    }

    public int getActivations() {
        return activations.get();
    }
}
