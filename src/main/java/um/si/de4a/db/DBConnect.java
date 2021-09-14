package um.si.de4a.db;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import um.si.de4a.AppConfig;
import um.si.de4a.util.CustomDE4ALogFormatter;
import um.si.de4a.util.DE4ALogger;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public final class DBConnect {
    public CouchDbConnector dbConnector;
    private static DBConnect db;
    private AppConfig appConfig;

    private DBConnect() throws IOException {
        Logger logger = DE4ALogger.getLogger();
        LogRecord logRecordInfo = new LogRecord(Level.INFO, "");
        LogRecord logRecordSevere = new LogRecord(Level.SEVERE, "");

        try {
            appConfig = new AppConfig();
          } catch (IOException e) {
            e.printStackTrace();
        }
        HttpClient httpClient = new StdHttpClient.Builder()
                .url(appConfig.getProperties().getProperty("db.ip.address").toString())
                .username(appConfig.getProperties().getProperty("db.username").toString())
                .password(appConfig.getProperties().getProperty("db.password").toString()).build();

        CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);

        try {
            this.dbConnector = dbInstance.createConnector(appConfig.getProperties().getProperty("db.name").toString(), true);
            logRecordInfo.setMessage("Established connection with internal database.");
            Object[] params = new Object[]{"Authority Agent DT", "Evidence Portal", "01006"};
            logRecordInfo.setParameters(params);
            logger.log(logRecordInfo);
        }
        catch (Exception ex){
            logRecordSevere.setMessage("Error in connecting to the internal database.");
            Object[] params = new Object[]{"Authority Agent DT", "Evidence Portal", "1006"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }
        /*finally {
            this.dbConnector.flushBulkBuffer();
            httpClient.shutdown();
        }*/
    }

    public static synchronized DBConnect getConnection() throws IOException {
        if(db == null){
            db = new DBConnect();
        }
        return db;
    }

}
