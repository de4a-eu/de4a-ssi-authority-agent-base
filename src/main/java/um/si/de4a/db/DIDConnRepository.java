package um.si.de4a.db;

import org.ektorp.CouchDbConnector;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@View( name = "all", map = "function(doc) { if (doc.type == 'DIDConn' ) emit( doc.userId, doc)}")
public class DIDConnRepository extends CouchDbRepositorySupport<DIDConn> {

    public DIDConnRepository(Class<DIDConn> type, CouchDbConnector db) {
        super(type, db);
        initStandardDesignDocument();
    }

    @View( name="byUserId", map = "function(doc) { if (doc.type == 'DIDConn') { emit(doc.userId, doc) } }")
    public List<DIDConn> findByUserId(String userId) {
        try {
            ViewQuery query = createQuery("byUserId")
                    .key(userId);
            List<DIDConn> result = db.queryView(query, DIDConn.class);
            System.out.println("Number of DIDConns in DB: " + result.size());
            if (result.size() > 0) {
                return result;
            }
        } catch (DocumentNotFoundException e) {
            return null;
        }

        return null;
    }

    @View( name="byInvitationId", map = "function(doc) { if (doc.type == 'DIDConn') { emit(doc.invitationId, doc) } }")
    public DIDConn findByInvitationId(String invitationId) {
        try {
            ViewQuery query = createQuery("byInvitationId")
                    .key(invitationId);
            List<DIDConn> result = db.queryView(query, DIDConn.class);
            if (result.size() > 0) {
                return result.get(result.size()-1);
            }
        } catch (DocumentNotFoundException e) {
            return null;
        }

        return null;
    }
}