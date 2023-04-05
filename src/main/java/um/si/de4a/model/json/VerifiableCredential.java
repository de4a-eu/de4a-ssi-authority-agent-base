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
package um.si.de4a.model.json;

import com.google.gson.annotations.SerializedName;

public class VerifiableCredential {
    @SerializedName("@context")
     private String[] context;
     private String id;
     private String[] type;
     private String issuer;
     private String issuanceDate;
     private String issued;
     private String validFrom;
     private String expirationDate;
     private CredentialSubject credentialSubject;
    // private CredentialStatus credentialStatus;
     private CredentialSchema credentialSchema;
    //  private Evidence evidence;


    public VerifiableCredential(String[] context, String id, String[] type, String issuer, String issuanceDate, String issued, String validFrom, String expirationDate, CredentialSubject credentialSubject, CredentialSchema credentialSchema) {
        this.context = context;
        this.id = id;
        this.type = type;
        this.issuer = issuer;
        this.issuanceDate = issuanceDate;
        this.issued = issued;
        this.validFrom = validFrom;
        this.expirationDate = expirationDate;
        this.credentialSubject = credentialSubject;
        this.credentialSchema = credentialSchema;
    }

    public String[] getContext() {
        return context;
    }

    public void setContext(String[] context) {
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getType() {
        return type;
    }

    public void setType(String[] type) {
        this.type = type;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getIssuanceDate() {
        return issuanceDate;
    }

    public void setIssuanceDate(String issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    public CredentialSubject getCredentialSubject() {
        return credentialSubject;
    }

    public void setCredentialSubject(CredentialSubject credentialSubject) {
        this.credentialSubject = credentialSubject;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        this.issued = issued;
    }

    public CredentialSchema getCredentialSchema() {
        return credentialSchema;
    }

    public void setCredentialSchema(CredentialSchema credentialSchema) {
        this.credentialSchema = credentialSchema;
    }
}
