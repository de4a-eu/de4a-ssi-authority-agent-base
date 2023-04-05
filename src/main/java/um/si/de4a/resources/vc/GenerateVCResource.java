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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.AppConfig;
import um.si.de4a.model.json.VerifiableCredential;
import um.si.de4a.model.xml.HigherEducationDiploma;
import um.si.de4a.util.DE4ALogger;
import um.si.de4a.util.XMLtoJSONAdapter;

import javax.ws.rs.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@Path("/generate-vc")
public class GenerateVCResource {
    private AppConfig appConfig = null;

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public VerifiableCredential generateVC(String vcData, String alias) throws IOException {
        Logger logger = DE4ALogger.getLogger();
        LogRecord logRecordInfo = new LogRecord(Level.INFO, "");
        LogRecord logRecordSevere = new LogRecord(Level.SEVERE, "");

        VerifiableCredential evidenceVC = null;
        JSONObject jsonObject = null;

        JSONParser jsonParser = new JSONParser();
        try {
            jsonObject = (JSONObject) jsonParser.parse(vcData);
        } catch (ParseException e) {
            logRecordSevere.setMessage("Object conversion error on Authority Agent: [GENERATE-VC] " + e.getMessage() + ".");
            Object[] params = new Object[]{"AAE03", alias};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);

        }

        if(jsonObject != null) {
            HigherEducationDiploma diploma = null;
            try {
                diploma = XMLtoJSONAdapter.convertXMLToPOJO(jsonObject.get("evidence").toString());
                System.out.println("SEND-OFFER: Converted input evidence in format: XML to format: POJO");
            }
            catch(Exception ex){
                logRecordSevere.setMessage("Object conversion error on Authority Agent: [GENERATE-VC] " + ex.getMessage() + ".");
                Object[] params = new Object[]{"AAE03", alias};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }
            if (diploma != null) {
                try {
                    evidenceVC = XMLtoJSONAdapter.convertPOJOtoJSON(diploma, jsonObject.get("publicDID").toString());
                    logRecordInfo.setMessage("SEND-OFFER: Converted input evidence in format: XML to format: JSON-LD");
                    Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "01008"};
                    logRecordInfo.setParameters(params);
                    logger.log(logRecordInfo);
                }
                catch(Exception ex){
                    logRecordSevere.setMessage("Object conversion error on Authority Agent: [GENERATE-VC] " + ex.getMessage() + ".");
                    Object[] params = new Object[]{"AAE03", alias};
                    logRecordSevere.setParameters(params);
                    logger.log(logRecordSevere);
                }
                //System.out.println("evidence vc: " + evidenceVC);
            }
        }

        return evidenceVC;
    }

}