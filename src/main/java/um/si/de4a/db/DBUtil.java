package um.si.de4a.db;

import org.ektorp.ViewQuery;

import java.net.MalformedURLException;
import java.util.List;

public class DBUtil {
    private DBConnect db = DBConnect.getConnection();
    private DIDConnRepository didConnRepository = null;

    public DBUtil() throws MalformedURLException {
        didConnRepository = new DIDConnRepository(DIDConn.class, db.dbConnector);
    }

    public boolean saveDIDConn(String userId, String invitationId, String invitationJSON){
        boolean dbStatus = false;
        DIDConn didConn = new DIDConn(null, null, userId, null, null,
                invitationId, invitationJSON, null, VCStatus.INVITATION_GENERATED, "DIDConn");

        didConnRepository.add(didConn);
        dbStatus = true;
        return dbStatus;
    }

    public List<DIDConn> getDIDConn(String userId){
        ViewQuery query = new ViewQuery()
                .designDocId("_design/DIDConn")
                .viewName("by_user_id")
                .key(userId);

        List<DIDConn> didConnList = db.dbConnector.queryView(query, DIDConn.class);
        return didConnList;
    }
}
