package org.practice.singleton.singleton1;

public class singleton1 {

    // volatile: without it, a thread outside the lock can see a half-constructed Config
    private static volatile Config config;

    public static Config getConfig() {
        Config local = config;              // one volatile read on the fast path
        if (local == null) {
            synchronized (singleton1.class) {
                local = config;
                if (local == null) {        // second check: another thread may have won the lock
                    config = local = new Config();
                }
            }
        }
        return local;
    }
}
