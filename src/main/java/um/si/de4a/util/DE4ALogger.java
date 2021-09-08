package um.si.de4a.util;

import um.si.de4a.db.DBConnect;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class DE4ALogger {
    private static DE4ALogger logger;
    private static Logger internalLogger;

    private DE4ALogger() {
        internalLogger = Logger.getLogger("DE4ALogger");
        internalLogger.setLevel(Level.CONFIG);

        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler("logs/de4a-metrics-log.txt", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        internalLogger.addHandler(fileHandler);
        fileHandler.setFormatter(new CustomDE4ALogFormatter());
    }

    public static synchronized Logger getLogger() throws IOException {
        if(logger == null){
            logger = new DE4ALogger();
        }
        return internalLogger;
    }
}
