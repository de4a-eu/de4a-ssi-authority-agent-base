package um.si.de4a.util;

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
        builder.append("DE4A METRICS - [").append(df.format(new Date(record.getMillis()))).append("] [")
                .append(record.getLevel()).append("] [").append(record.getParameters()[0]).append("] [1.3] [")
                .append(record.getParameters()[1]).append("] [").append(record.getParameters()[2])
                .append("] ").append(record.getMessage()).append(System.lineSeparator());
        return builder.toString();
    }
}
