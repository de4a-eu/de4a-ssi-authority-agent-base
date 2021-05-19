package um.si.de4a.db;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import um.si.de4a.AppConfig;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

public final class DBConnect {
    public CouchDbConnector dbConnector;
    private static DBConnect db;
    private AppConfig appConfig;

    private DBConnect() throws MalformedURLException {
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
            System.out.println("[DB CONNECT] Connected!");
        }
        catch (Exception ex){
            System.out.println("[DB CONNECT] Exception: " + ex.getMessage());
        }
        /*finally {
            this.dbConnector.flushBulkBuffer();
            httpClient.shutdown();
        }*/
    }

    public static synchronized DBConnect getConnection() throws MalformedURLException {
        if(db == null){
            db = new DBConnect();
        }
        return db;
    }

}
