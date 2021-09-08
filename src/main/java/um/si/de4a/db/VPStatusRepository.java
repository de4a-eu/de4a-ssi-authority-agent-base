package um.si.de4a.db;

import org.ektorp.CouchDbConnector;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.ViewQuery;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;

import java.util.List;

@View( name = "all", map = "function(doc) { if (doc.type == 'VPStatus' ) emit( doc.userId, doc)}")
public class VPStatusRepository extends CouchDbRepositorySupport<VPStatus> {

    protected VPStatusRepository(Class<VPStatus> type, CouchDbConnector db) {
        super(type, db);
        initStandardDesignDocument();
    }

    @View( name="byUserId", map = "function(doc) { if (doc.type == 'VPStatus') { emit(doc.userId, doc) } }")
    public VPStatus findByUserId(String userId) {
        try {
            ViewQuery query = createQuery("byUserId")
                    .key(userId);
            List<VPStatus> result = db.queryView(query, VPStatus.class);
            if(result.size() > 0)
                return result.get(result.size()-1); // get the most recent VP status
        } catch (DocumentNotFoundException e) {
            return null;
        }

        return null;
    }
}