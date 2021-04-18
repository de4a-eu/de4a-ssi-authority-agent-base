package um.si.de4a.db;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

import java.net.MalformedURLException;

public final class DBConnect {
    public CouchDbConnector dbConnector;
    private static DBConnect db;

    private DBConnect() throws MalformedURLException {
        HttpClient httpClient = new StdHttpClient.Builder()
                .url("http://164.8.250.43:5984/")
                .username("admin")
                .password("password").build();

        CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
        this.dbConnector = dbInstance.createConnector("de4a-authority-agent", true);
    }

    public static synchronized DBConnect getConnection() throws MalformedURLException {
        if(db == null){
            db = new DBConnect();
        }
        return db;
    }
}
