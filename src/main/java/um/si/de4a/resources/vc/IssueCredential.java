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
package um.si.de4a.resources.vc;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class IssueCredential {
    @SerializedName("credentials~attach")
    private ArrayList<CredentialsAttach> credentialsAttach = null;

    public IssueCredential(ArrayList<CredentialsAttach> credentialsAttach) {
        this.credentialsAttach = credentialsAttach;
    }

    public ArrayList<CredentialsAttach> getCredentialsAttach() {
        return credentialsAttach;
    }

    public void setCredentialsAttach(ArrayList<CredentialsAttach> credentialsAttach) {
        this.credentialsAttach = credentialsAttach;
    }
}
