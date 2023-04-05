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

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OfferCredential {
    private String comment;
    @SerializedName("credential_preview")
    private CredentialPreview credentialPreview;

    @SerializedName("offers~attach")
    private ArrayList<OffersAttach> offersAttach;

    public OfferCredential(String comment, CredentialPreview credentialPreview, ArrayList<OffersAttach> offersAttach) {
        this.comment = comment;
        this.credentialPreview = credentialPreview;
        this.offersAttach = offersAttach;
    }

    public CredentialPreview getCredentialPreview() {
        return credentialPreview;
    }

    public void setCredentialPreview(CredentialPreview credentialPreview) {
        this.credentialPreview = credentialPreview;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /*public OfferCredential(ArrayList<OffersAttach> offersAttach) {
        this.offersAttach = offersAttach;
    }*/

    public ArrayList<OffersAttach> getOffersAttach() {
        return offersAttach;
    }

    public void setOffersAttach(ArrayList<OffersAttach> offersAttach) {
        this.offersAttach = offersAttach;
    }
}
