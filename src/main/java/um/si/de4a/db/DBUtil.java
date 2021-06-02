package um.si.de4a.db;

import org.ektorp.ViewQuery;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class DBUtil {
    private DBConnect db = DBConnect.getConnection();
    private DIDConnRepository didConnRepository = null;
    private VCStatusRepository vcStatusRepository = null;
    private VPStatusRepository vpStatusRepository = null;

    private List<DIDConn> didConnList = null;
    private List<VCStatus> vcstatusList = null;
    private List<VPStatus> vpstatusList = null;

    public DBUtil() throws MalformedURLException {
        didConnRepository = new DIDConnRepository(DIDConn.class, db.dbConnector);
        vcStatusRepository = new VCStatusRepository(VCStatus.class, db.dbConnector);
        vpStatusRepository = new VPStatusRepository(VPStatus.class, db.dbConnector);
        didConnList = new ArrayList<>();
        vcstatusList = new ArrayList<>();
        vpstatusList = new ArrayList<>();
    }

    public boolean saveDIDConn(String userId, String invitationId, String invitationJSON, long statusChanged){
        boolean dbStatus = false;
        DIDConn didConn = new DIDConn(null, null, userId, null, null,
                invitationId, invitationJSON, null, DIDConnStatusEnum.INVITATION_GENERATED, "DIDConn", statusChanged);

        try {
            didConnRepository.add(didConn);
            dbStatus = true;
        }
        catch(Exception ex){
            dbStatus = false;
        }
        return dbStatus;
    }

    public DIDConn getDIDConnStatus(String userId){

        return didConnRepository.findByUserId(userId);
    }

    public boolean updateDIDConnection(String userID, String myDID, String theirDID, String connectionID, DIDConnStatusEnum status){
        boolean dbStatus = false;

        DIDConn userDIDConn = getDIDConnStatus(userID);
        userDIDConn.setMyDID(myDID);
        userDIDConn.setTheirDID(theirDID);
        userDIDConn.setConnectionId(connectionID);
        userDIDConn.setStatus(status);
        userDIDConn.setTimeUpdated(System.currentTimeMillis());
        try {
            didConnRepository.update(userDIDConn);
            dbStatus = true;
        }
        catch (Exception ex) {
            dbStatus = false;
        }

        return dbStatus;
    }

    public boolean saveVCStatus(String userID, String PIID, String VC, VCStatusEnum status){
        boolean dbStatus = false;
        VCStatus vcStatus = null;

        DIDConn userDIDConn = getDIDConnStatus(userID);
        if(userDIDConn != null){
            vcStatus = new VCStatus(null, null, userID, PIID, VC,
                    userDIDConn, status.OFFER_SENT,System.currentTimeMillis(),"VCStatus");
            try {
                vcStatusRepository.add(vcStatus);
                dbStatus = true;
            }
            catch(Exception ex){
                dbStatus = false;
            }
        }

        return dbStatus;
    }

    public VCStatus getVCStatus(String userID){
        return vcStatusRepository.findByUserId(userID);
    }

    public boolean updateVCStatus(String userID, VCStatusEnum status){
        boolean dbStatus = false;

        VCStatus vcStatus = getVCStatus(userID);
        vcStatus.setVCStatusEnum(status);
        vcStatus.setTimeUpdated(System.currentTimeMillis());
        try {
            vcStatusRepository.update(vcStatus);
            dbStatus = true;
        }
        catch (Exception ex) {
            dbStatus = false;
        }

        return dbStatus;
    }

    public boolean saveVPStatus(String userID, String PIID, String vpName, VPStatusEnum status){
        boolean dbStatus = false;
        VPStatus vpStatus = null;

        DIDConn userDIDConn = getDIDConnStatus(userID);
        if(userDIDConn != null){
            vpStatus = new VPStatus(null, null, userID, PIID,
                    userDIDConn,status.REQUEST_SENT,vpName,System.currentTimeMillis(),"VPStatus");
            try {
                vpStatusRepository.add(vpStatus);
                dbStatus = true;
            }
            catch(Exception ex){
                dbStatus = false;
            }
        }
        return dbStatus;
    }

    public VPStatus getVPStatus(String userID){
        return vpStatusRepository.findByUserId(userID);
    }

    public boolean updateVPStatus(String userID, VPStatusEnum status){
        boolean dbStatus = false;

        VPStatus vpStatus = getVPStatus(userID);
        vpStatus.setVPStatusEnum(status);
        vpStatus.setTimeUpdated(System.currentTimeMillis());
        try {
            vpStatusRepository.update(vpStatus);
            dbStatus = true;
        }
        catch (Exception ex) {
            dbStatus = false;
        }
        return dbStatus;
    }

    public List<VPStatus> findVPById(String vpId){
        ViewQuery query = new ViewQuery()
                .designDocId("_design/VPStatus")
                .viewName("by_vp_id")
                .key(vpId);

        try {
            vpstatusList = db.dbConnector.queryView(query, VPStatus.class);
        }
        catch(Exception ex){
            System.out.println("[FIND BY VP ID] Exception: " + ex.getMessage());
        }
        System.out.println("[VPStatus] size: " + vpstatusList.size());

        return vpstatusList;
    }
}
