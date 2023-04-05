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

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CustomDE4ALogFormatter extends Formatter {
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder(1000);
        builder.append("[").append(df.format(new Date(record.getMillis()))).append("] [")
                .append(record.getLevel()).append("] [").append(record.getParameters()[0])
                .append("] [").append(new String(record.getParameters()[1].toString().getBytes(StandardCharsets.ISO_8859_1)))
                .append("] [UC#1.3] ").append(record.getMessage()).append(System.lineSeparator());
        return builder.toString();
    }
}
