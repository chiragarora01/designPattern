package org.practice.singleton.singleton2;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator implements Serializable {
    private static final long serialVersionUID = 1L;
    private static volatile IdGenerator INSTANCE;
    private final AtomicInteger integer = new AtomicInteger(0);

    private IdGenerator() {
        // blocks reflection: once the instance exists, no second construction
        if (INSTANCE != null) {
            throw new IllegalStateException("Use getIdGenerator()");
        }
    }

    public static IdGenerator getIdGenerator() {
        if (INSTANCE == null) {
            synchronized (IdGenerator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new IdGenerator();
                }
            }
        }
        return INSTANCE;
    }

    // serialization hook: return the one instance instead of a freshly deserialized one
    private Object readResolve() {
        return getIdGenerator();
    }

    public int getId() {
        return integer.incrementAndGet();
    }
}
