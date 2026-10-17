package org.practice.singleton.singleton1;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class Config {
    private static final AtomicInteger LOAD_COUNT = new AtomicInteger();

    private final Properties props = new Properties();

    public Config() {
        LOAD_COUNT.incrementAndGet();
        try (InputStream in = Config.class.getResourceAsStream("/config.properties")) {
            if (in == null) {
                throw new IllegalStateException("config.properties not found on classpath");
            }
            props.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load config.properties", e);
        }
    }

    public String get(String key) {
        return props.getProperty(key);
    }

    public static int loadCount() {
        return LOAD_COUNT.get();
    }
}
