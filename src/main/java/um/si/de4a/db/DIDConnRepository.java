package um.si.de4a.db;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewResult;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;

import java.util.List;

public class DIDConnRepository extends CouchDbRepositorySupport<DIDConn> {

    protected DIDConnRepository(Class<DIDConn> type, CouchDbConnector db) {
        super(type, db);
    }


}