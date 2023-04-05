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
public class DIDConn extends CouchDbDocument {
    @JsonProperty("_id")
    private String id;

    @JsonProperty("_rev")
    private String revision;

    private String userId;

    private String myDID;

    private String theirDID;

    private String invitationId;

    private String invitationJSON;

    private String connectionId;

    private DIDConnStatusEnum didConnStatusEnum;

    private long timeCreated;

    private long timeDeleted;

    @TypeDiscriminator
    private String type;

    public DIDConn() {
    }

    public DIDConn(String id, String revision, String userId, String myDID, String theirDID, String invitationId, String invitationJSON, String connectionId, DIDConnStatusEnum didConnStatusEnum, String type, long timeCreated, long timeDeleted) {
        this.id = id;
        this.revision = revision;
        this.userId = userId;
        this.myDID = myDID;
        this.theirDID = theirDID;
        this.invitationId = invitationId;
        this.invitationJSON = invitationJSON;
        this.connectionId = connectionId;
        this.didConnStatusEnum = didConnStatusEnum;
        this.type = type;
        this.timeCreated = timeCreated;
        this.timeDeleted = timeDeleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMyDID() {
        return myDID;
    }

    public void setMyDID(String myDID) {
        this.myDID = myDID;
    }

    public String getTheirDID() {
        return theirDID;
    }

    public void setTheirDID(String theirDID) {
        this.theirDID = theirDID;
    }

    public String getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(String invitationId) {
        this.invitationId = invitationId;
    }

    public String getInvitationJSON() {
        return invitationJSON;
    }

    public void setInvitationJSON(String invitationJSON) {
        this.invitationJSON = invitationJSON;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public DIDConnStatusEnum getStatus() {
        return didConnStatusEnum;
    }

    public void setStatus(DIDConnStatusEnum didConnStatusEnum) {
        this.didConnStatusEnum = didConnStatusEnum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public long getTimeDeleted() {
        return timeDeleted;
    }

    public void setTimeDeleted(long timeDeleted) {
        this.timeDeleted = timeDeleted;
    }
}




