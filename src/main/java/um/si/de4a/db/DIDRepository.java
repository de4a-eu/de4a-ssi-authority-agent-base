package um.si.de4a.db;

import org.ektorp.CouchDbConnector;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.ViewQuery;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;

import java.util.List;

@View( name = "all", map = "function(doc) { if (doc.type == 'DID' ) emit( doc.value, doc)}")
public class DIDRepository extends CouchDbRepositorySupport<DID> {
    protected DIDRepository(Class<DID> type, CouchDbConnector db) {
        super(type, db);
        initStandardDesignDocument();
    }
}
