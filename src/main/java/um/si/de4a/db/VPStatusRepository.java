/*
 * Copyright (C) 2023, Partners of the EU funded DE4A project consortium
 *   (https://www.de4a.eu/consortium), under Grant Agreement No.870635
 * Author: University of Maribor (UM)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    @View( name="byPiid", map = "function(doc) { if (doc.type == 'VPStatus') { emit(doc.piid, doc) } }")
    public VPStatus findByPiid(String piid) {
        try {
            ViewQuery query = createQuery("byPiid")
                    .key(piid);
            List<VPStatus> result = db.queryView(query, VPStatus.class);
            if(result.size() > 0)
                return result.get(result.size()-1); // get the most recent VP status
        } catch (DocumentNotFoundException e) {
            return null;
        }

        return null;
    }
}