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
package um.si.de4a.resources.offer;

import um.si.de4a.model.json.VerifiableCredential;

public class SignRequest {
    private String created;
    private VerifiableCredential credential;
    private String did;
    private String signatureType;

    public SignRequest(String created, VerifiableCredential credential, String did, String signatureType) {
        this.created = created;
        this.credential = credential;
        this.did = did;
        this.signatureType = signatureType;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public VerifiableCredential getCredential() {
        return credential;
    }

    public void setCredential(VerifiableCredential credential) {
        this.credential = credential;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getSignatureType() {
        return signatureType;
    }

    public void setSignatureType(String signatureType) {
        this.signatureType = signatureType;
    }
}
