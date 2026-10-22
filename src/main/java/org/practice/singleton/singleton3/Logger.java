package org.practice.singleton.singleton3;
import java.io.PrintStream;
public class Logger {

    private static volatile Logger INSTANCE;
    private final PrintStream out;

    private Logger(PrintStream out) {
        this.out = out;
    }

    public static Logger getINSTANCE() {
        if (INSTANCE == null) {
            synchronized (Logger.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Logger(System.out);
                }
            }
        }
        return INSTANCE;
    }

    // package-private: makes NEW logger, never touches INSTANCE
    static Logger newInstanceForTest(PrintStream out) { return new Logger(out); }


    public void log(String message) {
        out.println(message);
    }
}
