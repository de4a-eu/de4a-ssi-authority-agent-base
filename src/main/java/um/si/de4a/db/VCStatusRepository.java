package um.si.de4a.db;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;

public class VCStatusRepository extends CouchDbRepositorySupport<VCStatus> {

    protected VCStatusRepository(Class<VCStatus> type, CouchDbConnector db) {
        super(type, db);
    }

}