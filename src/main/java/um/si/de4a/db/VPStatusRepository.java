package um.si.de4a.db;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;

public class VPStatusRepository extends CouchDbRepositorySupport<VPStatus> {

    protected VPStatusRepository(Class<VPStatus> type, CouchDbConnector db) {
        super(type, db);
    }

}