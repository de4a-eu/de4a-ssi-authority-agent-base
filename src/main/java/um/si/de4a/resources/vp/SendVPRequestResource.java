package um.si.de4a.resources.vp;

import com.google.gson.Gson;
import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.DIDConn;
import um.si.de4a.db.VPStatusEnum;
import um.si.de4a.util.DE4ALogger;

import javax.ws.rs.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@Path("/send-vp-request")
public class SendVPRequestResource {
    @POST
    @Consumes("application/json")
    @Produces("application/json")
     public boolean sendVPRequest(String vpRequest) throws IOException, ParseException {
        Logger logger = DE4ALogger.getLogger();
        LogRecord logRecordInfo = new LogRecord(Level.INFO, "");
        LogRecord logRecordSevere = new LogRecord(Level.SEVERE, "");

        boolean vpRequestStatus = false;
        JSONObject jsonRequest = null;
        DBUtil dbUtil = new DBUtil();

        JSONParser jsonParser = new JSONParser();
        try {
            jsonRequest = (JSONObject) jsonParser.parse(vpRequest);
            logRecordInfo.setMessage("Received input eIDAS user data.");
            Object[] params = new Object[]{"Authority Agent DR", "eProcedure Portal DE", "01001"};
            logRecordInfo.setParameters(params);
            logger.log(logRecordInfo);
        } catch (ParseException e) {
            logRecordSevere.setMessage("Error parsing input eIDAS data.");
            Object[] params = new Object[]{"Authority Agent DR", "eProcedure Portal DE", "1001"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
            //e.printStackTrace();
        }

        if(jsonRequest != null) {

            String userID = "", presentationFormat = "";
            try{
                userID = jsonRequest.get("userId").toString();
                presentationFormat = jsonRequest.get("presentationFormat").toString();
            }
            catch(Exception ex){
                logRecordSevere.setMessage("Error parsing input parameters.");
                Object[] params = new Object[]{"Authority Agent DR", "eProcedure Portal DE", "1005"};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }

            // DONE: call database getDIDConnStatus(userID): DIDConn object
            DIDConn userDIDConn = null;
            try {
                userDIDConn = dbUtil.getDIDConnStatus(userID);
            }
            catch(Exception ex){
                logRecordSevere.setMessage("Error accessing data on Authority Agent DR.");
                Object[] params = new Object[]{"Authority Agent DR", "eProcedure Portal DE", "1010"};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }

            if(userDIDConn != null) { // if invitation is generated
                if (!userDIDConn.getConnectionId().equals("")) { // if DIDConn is established
                    // DONE: generate VPRequest (format, myDID, theirDID): VPRequest object
                    /*String format = new JsonObj
                            (new String[]{"https://www.w3.org/2018/credentials/v1", "https://www.w3.org/2018/credentials/examples/v1"},

                                     new String[]{"VerifiablePresentation", "CredentialManagerPresentation"}).toString();*/

                    /*RequestPresentationAttachObj rpa = null;

                    try{
                        rpa = new RequestPresentationAttachObj(new DataObj(Base64.getEncoder().encodeToString(format.getBytes(StandardCharsets.UTF_8))));
                    }
                    catch(Exception ex){
                        logRecordSevere.setMessage( "Object conversion error on Authority Agent DR.");
                        Object[] params = new Object[]{"Authority Agent DR", "Aries Government Agent", "1008"};
                        logRecordSevere.setParameters(params);
                        logger.log(logRecordSevere);
                    }

                    ArrayList<RequestPresentationAttachObj> rpaList = new ArrayList<>();
                    rpaList.add(rpa);
                    VPRequest vpRequestObj = new VPRequest(userDIDConn.getMyDID(), new RequestPresentationObj(rpaList), userDIDConn.getTheirDID());
                    */

                    DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US);

                    String outputLastModTime = "";
                    try {
                        Calendar cal = Calendar.getInstance();
                        outputLastModTime = outputFormat.format(cal.getTime());
                    } catch (Exception ex) {
                        logRecordSevere.setMessage("Object conversion error on Authority Agent DR.");
                        Object[] params = new Object[]{"Authority Agent DR", "Aries Government Agent", "1008"};
                        logRecordSevere.setParameters(params);
                        logger.log(logRecordSevere);
                    }
                    String lastModTime = outputLastModTime;
                    String randomID = UUID.randomUUID().toString();
                    ArrayList<Format> formats = new ArrayList<Format>(){{add(new Format(randomID, "dif/presentation-exchange/definition@v1.0"));}};

                    ArrayList<Schema> schemas = new ArrayList<>();
                    schemas.add(new Schema("https://www.w3.org/2018/credentials/v1"));
                    schemas.add(new Schema("https://www.w3.org/2018/credentials/examples/v1"));

                    ArrayList<Constraint> constraints = new ArrayList<>();
                    ArrayList<String> paths = new ArrayList<>();
                    paths.add("$.type");

                    Filter filter = new Filter("string", presentationFormat);
                    constraints.add(new Constraint(new ArrayList<Field>(){{
                        add(new Field(paths, filter));
                    }}));

                    VPAttachment vpAttachment = new VPAttachment(new Option(UUID.randomUUID().toString(), "de4a.eu/VerifiablePresentation"),
                            new PresentationDefinition(new ArrayList<InputDescriptor>(){{
                                add(new InputDescriptor("vp_type", "VP Type", schemas, constraints));
                            }}));

                    Gson gson = new Gson();
                    ArrayList<RequestVPAttach> vpAttachList = null;
                    try{
                        vpAttachList = new ArrayList<RequestVPAttach>(){{
                            add(new RequestVPAttach(randomID,  new Data(Base64.getEncoder().encodeToString(gson.toJson(vpAttachment).getBytes(StandardCharsets.UTF_8))), lastModTime, "application/octet-stream"));
                        }};
                    }
                    catch (Exception ex){
                        logRecordSevere.setMessage("Object conversion error on Authority Agent DR.");
                        Object[] params = new Object[]{"Authority Agent DR", "Aries Government Agent", "1008"};
                        logRecordSevere.setParameters(params);
                        logger.log(logRecordSevere);
                    }

                    RequestPresentationObj requestPresentationObj = new RequestPresentationObj("Request DE4A UC3 presentation.", formats, vpAttachList);
                    VPRequest vpRequestObj = new VPRequest(userDIDConn.getMyDID(), requestPresentationObj, userDIDConn.getTheirDID());

                    // DONE: call Aries /presentproof/send-request-presentation(VPRequest):  PIID
                    AriesUtil ariesUtil = new AriesUtil();
                    String piid = "";
                    try{
                        piid = ariesUtil.sendRequest(vpRequestObj);
                        if (piid != "") {
                            //System.out.println("[SEND VP REQUEST] Received PIID: " + piid);

                            // DONE: call database saveVPStatus(userId, PIID, status: request_sent): boolean
                            boolean response = false;
                            try{
                                response = dbUtil.saveVPStatus(userID, piid, null, VPStatusEnum.REQUEST_SENT);
                                logRecordInfo.setMessage("Stored current state in the Authority Agent DR database.");
                                Object[] params = new Object[]{"Authority Agent DR", "eProcedure Portal DE", "01006"};
                                logRecordInfo.setParameters(params);
                                logger.log(logRecordInfo);
                            }
                            catch(Exception ex){
                                logRecordSevere.setMessage("Error saving data on Authority Agent DR.");
                                Object[] params = new Object[]{"Authority Agent DR", "eProcedure Portal DE", "1001"};
                                logRecordSevere.setParameters(params);
                                logger.log(logRecordSevere);
                            }

                            if (response == true)
                                vpRequestStatus = true;
                        }
                    }
                   catch(Exception ex){
                       logRecordSevere.setMessage( "Error on response from the Aries Government Agent.");
                       Object[] params = new Object[]{"Authority Agent DR", "Aries Government Agent", "1002"};
                       logRecordSevere.setParameters(params);
                       logger.log(logRecordSevere);
                   }
                }
            }
        }
        return vpRequestStatus;
    }
}
