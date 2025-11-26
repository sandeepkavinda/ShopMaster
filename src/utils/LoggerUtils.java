package utils;

import java.io.IOException;
import java.util.logging.*;

public class LoggerUtils {
    private static Logger logger;

    static {
        try {
            LogManager.getLogManager().readConfiguration(LoggerUtils.class.getResourceAsStream("/logging.properties")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger = Logger.getLogger("MyAppLogger");
    }

    public static Logger getLogger() {
        return logger;
    }
}
