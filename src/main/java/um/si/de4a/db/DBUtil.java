package um.si.de4a.db;

import org.ektorp.ViewResult;
import org.ektorp.support.GenerateView;

import java.net.MalformedURLException;

public class DBUtil {
    private DBConnect db = DBConnect.getConnection();
    private DIDConnRepository didConnRepository = null;

    public DBUtil() throws MalformedURLException {
        didConnRepository = new DIDConnRepository(DIDConn.class, db.dbConnector);
    }

    public boolean saveDIDConn(String userId, String invitationId, String invitationJSON){
        boolean dbStatus = false;
        DIDConn didConn = new DIDConn(userId,invitationId, invitationJSON, Status.INVITATION_GENERATED, "DIDConn");

        didConnRepository.add(didConn);
        dbStatus = true;
        return dbStatus;
    }

}
