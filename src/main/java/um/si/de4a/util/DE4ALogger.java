/*
 * Copyright (C) 2023, Partners of the EU funded DE4A project consortium
 *   (https://www.de4a.eu/consortium), under Grant Agreement No.870635
 * Author: University of Maribor (UM)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
            fileHandler = new FileHandler("/usr/local/tomcat/logs/de4a-metrics-log.txt", true);
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
