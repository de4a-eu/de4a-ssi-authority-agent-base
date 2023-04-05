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

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;

@JsonIgnoreProperties({"id", "revision"})
public class VCStatus extends CouchDbDocument {
    @JsonProperty("_id")
    private String id;

    @JsonProperty("_rev")
    private String revision;

    private String userId;

    private String piid;

    private String vc;

    private DIDConn didConn;

    private VCStatusEnum VCStatusEnum;

    private long timeUpdated;

    @TypeDiscriminator
    private String type;

    public VCStatus() {
    }

    public VCStatus(String id, String revision, String userId, String piid, String vc, DIDConn didConn, um.si.de4a.db.VCStatusEnum VCStatusEnum, long timeUpdated, String type) {
        this.id = id;
        this.revision = revision;
        this.userId = userId;
        this.piid = piid;
        this.vc = vc;
        this.didConn = didConn;
        this.VCStatusEnum = VCStatusEnum;
        this.timeUpdated = timeUpdated;
        this.type = type;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getRevision() {
        return revision;
    }

    @Override
    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPiid() {
        return piid;
    }

    public void setPiid(String piid) {
        this.piid = piid;
    }

    public String getVc() {
        return vc;
    }

    public void setVc(String vc) {
        this.vc = vc;
    }

    public DIDConn getDidConn() {
        return didConn;
    }

    public void setDidConn(DIDConn didConn) {
        this.didConn = didConn;
    }

    public um.si.de4a.db.VCStatusEnum getVCStatusEnum() {
        return VCStatusEnum;
    }

    public void setVCStatusEnum(um.si.de4a.db.VCStatusEnum VCStatusEnum) {
        this.VCStatusEnum = VCStatusEnum;
    }

    public long getTimeUpdated() {
        return timeUpdated;
    }

    public void setTimeUpdated(long timeUpdated) {
        this.timeUpdated = timeUpdated;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}




