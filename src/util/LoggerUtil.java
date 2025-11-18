package util;

import java.io.IOException;
import java.util.logging.*;

public class LoggerUtil {
    private static Logger logger;

    static {
        try {
            LogManager.getLogManager().readConfiguration(
                LoggerUtil.class.getResourceAsStream("/logging.properties")
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
