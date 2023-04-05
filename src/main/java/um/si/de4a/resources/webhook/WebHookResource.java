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
package um.si.de4a.resources.webhook;

import jnr.ffi.annotations.Encoding;
import org.jboss.resteasy.annotations.ContentEncoding;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.AppConfig;
import um.si.de4a.model.webhook.MessageFactory;
import um.si.de4a.model.webhook.WebhookMessage;
import um.si.de4a.util.DE4ALogger;

import javax.ws.rs.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@Path("/webhook")
public class WebHookResource {

    private AppConfig appConfig = null;

    @POST
    @Consumes({"application/json", "text/plain", "text/html", "application/x-www-form-urlencoded", "*/*"})
    @Produces({"application/json", "text/plain", "text/html", "application/x-www-form-urlencoded", "*/*"})
    public void receiveWebhookInfo(String contents) throws IOException, ParseException {

       // System.out.println("Received webhook message: " + contents.toString());
        JSONParser jsonParser = new JSONParser();

        JSONObject jsonContents = (JSONObject) jsonParser.parse(contents);
        String topicName = jsonContents.get("topic").toString();
        String msgContent = jsonContents.get("message").toString();

        MessageFactory msgFactory = new MessageFactory();
        WebhookMessage webhookMessage = msgFactory.getMessageHandler(topicName);

        webhookMessage.updateStatus(msgContent);

        //appConfig = new AppConfig();

    }
}

