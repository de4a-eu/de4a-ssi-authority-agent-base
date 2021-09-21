package um.si.de4a.resources.vc;

import com.google.gson.Gson;
import org.apache.commons.lang3.SerializationUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.VCStatus;
import um.si.de4a.db.VCStatusEnum;
import um.si.de4a.model.json.SignedVerifiableCredential;
import um.si.de4a.util.DE4ALogger;

import javax.ws.rs.*;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@Path("/send-vc")
public class SendVCResource {
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public boolean sendVC(String user) throws IOException, java.text.ParseException {
        Logger logger = DE4ALogger.getLogger();
        LogRecord logRecordInfo = new LogRecord(Level.INFO, "");
        LogRecord logRecordSevere = new LogRecord(Level.SEVERE, "");

        boolean vcStatusResult = false, vcAcceptStatus = false;
        JSONObject jsonRequest = null;

        DBUtil dbUtil = new DBUtil();
        VCStatus vcStatus = null;

        JSONParser jsonParser = new JSONParser();
        try {
            jsonRequest = (JSONObject) jsonParser.parse(user);
            logRecordInfo.setMessage("SEND-VC: Received input eIDAS user data.");
            Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "0201"};
            logRecordInfo.setParameters(params);
            logger.log(logRecordInfo);
        } catch (ParseException e) {
            logRecordSevere.setMessage("SEND-VC: Object conversion error on Authority Agent DT.");
            Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "20005"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }

        if(jsonRequest != null) {
            String userID = "";
            try {
                userID = jsonRequest.get("userId").toString();
                logRecordInfo.setMessage("SEND-VC: Received input userId data.");
                Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "0202"};
                logRecordInfo.setParameters(params);
                logger.log(logRecordInfo);
            }
            catch(Exception ex){
                logRecordSevere.setMessage("SEND-VC: Object conversion error on Authority Agent DT.");
                Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "20005"};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }

            // DONE: call database getVCStatus(userID): status, piid
            try {
                vcStatus = dbUtil.getVCStatus(userID);

                logRecordInfo.setMessage("SEND-VC: Received user VCStatus status data.");
                Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "0104"};
                logRecordInfo.setParameters(params);
                logger.log(logRecordInfo);
            } catch (Exception ex) {
                logRecordSevere.setMessage( "SEND-VC: Error accessing data on Authority Agent DT.");
                Object[] params = new Object[]{"Authority Agent DT", "Evidence Portal DO", "20006"};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }

            AriesUtil ariesUtil = new AriesUtil();
            Gson gson = new Gson();

            SignedVerifiableCredential credential = null;
            try {
                credential = gson.fromJson(vcStatus.getVc(), SignedVerifiableCredential.class);
            }
            catch(Exception ex){
                logRecordSevere.setMessage("SEND-VC: Object conversion error on Authority Agent DT.");
                Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "20005"};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }

            DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US);

            String outputLastModTime = "";
            try {
                Calendar cal = Calendar.getInstance();
                outputLastModTime = outputFormat.format(cal.getTime());
            }
            catch(Exception ex){
                logRecordSevere.setMessage("SEND-VC: Object conversion error on Authority Agent DT.");
                Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "20005"};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }
            if (vcStatus != null) {
                try {
                    Data data = null;
                    try {
                        data = new Data(Base64.getEncoder().encodeToString(gson.toJson(credential).getBytes(StandardCharsets.UTF_8)));
                    }
                    catch(Exception ex){
                        logRecordSevere.setMessage("SEND-VC: Object conversion error on Authority Agent DT.");
                        Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "20005"};
                        logRecordSevere.setParameters(params);
                        logger.log(logRecordSevere);
                    }

                    CredentialsAttach credentialsAttach = new CredentialsAttach(outputLastModTime, data);
                    IssueCredential issueCredential = new IssueCredential(new ArrayList<CredentialsAttach>(){{add(credentialsAttach);}});
                    SendVCRequest request = new SendVCRequest(issueCredential);

                    //System.out.println("[SEND VC] Signed VC ID: " + request.getCredential().getCredentialsAttach().get(0).getData().getJson().getId());
                    // DONE: call Aries /issuecredential/{PIID}/accept-request(VC):  null/empty
                    try {
                        vcAcceptStatus = ariesUtil.acceptRequest(vcStatus.getPiid(), request);
                    }
                    catch(Exception ex){
                        logRecordSevere.setMessage( "SEND-VC: Error on response from the Aries Government Agent.");
                        Object[] params = new Object[]{"Authority Agent DT", "Aries Government Agent", "10704"};
                        logRecordSevere.setParameters(params);
                        logger.log(logRecordSevere);
                    }

                    // DONE: call database updateVCStatus(userId, status: vc_sent): boolean
                    if (vcAcceptStatus == true) {
                        try {
                            dbUtil.updateVCStatus(userID, VCStatusEnum.VC_SENT);
                            logRecordInfo.setMessage("SEND-VC: Stored current state in the Authority Agent DT internal database.");
                            Object[] params = new Object[]{"Authority Agent DT", "Evidence Portal", "0103"};
                            logRecordInfo.setParameters(params);
                            logger.log(logRecordInfo);
                        }
                        catch(Exception ex){
                            logRecordSevere.setMessage( "SEND-VC: Error saving data on Authority Agent DT.");
                            Object[] params = new Object[]{"Authority Agent DT", "Evidence Portal DO", "20006"};
                            logRecordSevere.setParameters(params);
                            logger.log(logRecordSevere);
                        }
                        vcStatusResult = true;
                    }

                } catch (Exception ex) {
                    logRecordSevere.setMessage("SEND-VC: Object conversion error on Authority Agent DT.");
                    Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "20005"};
                    logRecordSevere.setParameters(params);
                    logger.log(logRecordSevere);
                    //System.out.println("[SEND-VC] Exception: " + ex.getMessage());
                }
            }
        }
        return vcStatusResult;
    }
}
