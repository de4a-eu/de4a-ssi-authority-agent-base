package um.si.de4a.db;

import org.ektorp.CouchDbConnector;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.ViewQuery;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;

import java.util.List;

@View( name = "all", map = "function(doc) { if (doc.type == 'VCStatus' ) emit( doc.userId, doc)}")
public class VCStatusRepository extends CouchDbRepositorySupport<VCStatus> {

    protected VCStatusRepository(Class<VCStatus> type, CouchDbConnector db) {
        super(type, db);
        initStandardDesignDocument();
    }

    @View( name="byUserId", map = "function(doc) { if (doc.type == 'VCStatus') { emit(doc.userId, doc) } }")
    public VCStatus findByUserId(String userId) {
        try {
            ViewQuery query = createQuery("byUserId")
                    .key(userId);
            List<VCStatus> result = db.queryView(query, VCStatus.class);
            if (result.size() > 0) {
                return result.get(result.size()-1);
                //return result.get(result.size()-1); // get the most recent VC status
            }
        } catch (DocumentNotFoundException e) {
            return null;
        }

        return null;
    }

    @View( name="byPiid", map = "function(doc) { if (doc.type == 'VCStatus') { emit(doc.piid, doc) } }")
    public VCStatus findByPiid(String piid) {
        try {
            ViewQuery query = createQuery("byPiid")
                    .key(piid);
            List<VCStatus> result = db.queryView(query, VCStatus.class);
            if (result.size() > 0) {
                return result.get(result.size()-1);
                //return result.get(result.size()-1); // get the most recent VC status
            }
        } catch (DocumentNotFoundException e) {
            return null;
        }

        return null;
    }
}