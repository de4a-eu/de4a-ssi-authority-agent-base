package um.si.de4a.db;

import org.ektorp.CouchDbConnector;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;

import java.util.Iterator;
import java.util.List;

@View( name = "all", map = "function(doc) { if (doc.type == 'DIDConn' ) emit( doc.userId, doc)}")
public class DIDConnRepository extends CouchDbRepositorySupport<DIDConn> {

    public DIDConnRepository(Class<DIDConn> type, CouchDbConnector db) {
        super(type, db);
        initStandardDesignDocument();
    }

    @View( name="byUserId", map = "function(doc) { if (doc.type == 'DIDConn') { emit(doc.userId, doc) } }")
    public DIDConn findByUserId(String userId) {
        try {
            ViewQuery query = createQuery("byUserId")
                    .key(userId);
            List<DIDConn> result = db.queryView(query, DIDConn.class);
            if (result.size() > 0) {
                return result.get(0);
            }
        } catch (DocumentNotFoundException e) {
            return null;
        }

        return null;
    }
}