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
package um.si.de4a.model.webhook;

import java.io.IOException;

public class MessageFactory {
    public WebhookMessage getMessageHandler(String messageType) throws IOException {
        if(messageType == null)
            return null;
        if(messageType.equalsIgnoreCase("didexchange_states")){
            return new DIDConnMessage();
        }
        else if(messageType.equalsIgnoreCase("issue-credential_actions") || messageType.equalsIgnoreCase("issue-credential_states")){
            return new IssueVCMessage();
        }
        else if(messageType.equalsIgnoreCase("present-proof_actions") || messageType.equalsIgnoreCase("present-proof_states")){
            return new SubmitVPMessage();
        }
        return null;
    }
}
