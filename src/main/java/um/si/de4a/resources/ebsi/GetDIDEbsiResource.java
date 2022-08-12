package um.si.de4a.resources.ebsi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import um.si.de4a.AppConfig;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.VCStatus;
import um.si.de4a.db.VCStatusEnum;
import um.si.de4a.util.DE4ALogger;

import javax.ws.rs.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@Path("/get-did-ebsi")
public class GetDIDEbsiResource {

    private AppConfig appConfig = null;

    @GET
    @Consumes("application/json")
    @Produces("application/json")
    public String getDID() throws IOException, ParseException {
        Logger logger = DE4ALogger.getLogger();
        LogRecord logRecordInfo = new LogRecord(Level.INFO, "");
        LogRecord logRecordSevere = new LogRecord(Level.SEVERE, "");

        String alias = "";
        appConfig = new AppConfig();
        try {
            alias = appConfig.getProperties().getProperty("alias");
        }
        catch (Exception ex){
            logRecordSevere.setMessage( "Configuration error occurred on Authority Agent.");
            Object[] params = new Object[]{"AAE09", alias};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }

        DBUtil dbUtil = new DBUtil();
        String did = "";

        DIDEbsiObject didEbsiObject = null;
        try {
            did = dbUtil.getDID();
            didEbsiObject = new DIDEbsiObject(did);
        }
        catch(Exception ex){
            logRecordSevere.setMessage("Error accessing data on Authority Agent internal database: [GET-DID-EBSI] " + ex.getMessage() + ".");
            Object[] params = new Object[]{"AAE04", alias};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonDIDObject = mapper.writeValueAsString(didEbsiObject);
        return jsonDIDObject;
    }
}
