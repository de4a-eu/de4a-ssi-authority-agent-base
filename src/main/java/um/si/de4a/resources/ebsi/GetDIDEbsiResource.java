package um.si.de4a.resources.ebsi;

<<<<<<< HEAD
import com.fasterxml.jackson.databind.ObjectMapper;
=======
>>>>>>> 31cf0664de392906ae2c6385a6559e1ed233602f
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
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
    @GET
    @Consumes("application/json")
    @Produces("application/json")
    public String getDID() throws IOException, ParseException {
        Logger logger = DE4ALogger.getLogger();
        LogRecord logRecordInfo = new LogRecord(Level.INFO, "");
        LogRecord logRecordSevere = new LogRecord(Level.SEVERE, "");

        DBUtil dbUtil = new DBUtil();
        String did = "";

<<<<<<< HEAD
        DIDEbsiObject didEbsiObject = null;
        try {
            did = dbUtil.getDID();
            didEbsiObject = new DIDEbsiObject(did);
=======
        try {
            did = dbUtil.getDID();
>>>>>>> 31cf0664de392906ae2c6385a6559e1ed233602f
        }
        catch(Exception ex){
            logRecordSevere.setMessage("Error accessing data on Authority Agent DT.");
            Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "1010"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }

<<<<<<< HEAD
        ObjectMapper mapper = new ObjectMapper();
        String jsonDIDObject = mapper.writeValueAsString(didEbsiObject);
        return jsonDIDObject;
=======
        return did;
>>>>>>> 31cf0664de392906ae2c6385a6559e1ed233602f
    }
}
