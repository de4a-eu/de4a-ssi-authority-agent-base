package um.si.de4a.resources.vc;

import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.AppConfig;
import um.si.de4a.model.json.VerifiableCredential;
import um.si.de4a.model.json.VerifiableCredentialUpdated;
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
    public VerifiableCredentialUpdated generateVC(String vcData, String alias) throws IOException {
        Logger logger = DE4ALogger.getLogger();
        LogRecord logRecordInfo = new LogRecord(Level.INFO, "");
        LogRecord logRecordSevere = new LogRecord(Level.SEVERE, "");

        VerifiableCredentialUpdated evidenceVC = null;
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