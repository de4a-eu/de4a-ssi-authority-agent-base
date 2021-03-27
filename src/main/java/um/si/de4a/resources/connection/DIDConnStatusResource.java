package um.si.de4a.resources.connection;


import um.si.de4a.db.DBUtil;
import um.si.de4a.db.DIDConn;
import um.si.de4a.db.Status;

import javax.ws.rs.*;
import java.net.MalformedURLException;
import java.util.List;

@Path("/did-conn-status")
public class DIDConnStatusResource {

    @GET
    @Consumes("text/plain")
    @Produces("text/plain")
    public int fetch(@QueryParam("userId")String userID) throws MalformedURLException {
        int connectionStatusCode = 0;

        //call database getDIDConn (userID): invitationID, connectionID, status
        DBUtil dbUtil = new DBUtil();
        DIDConn userDidConn = dbUtil.getDIDConn(userID).get(0);

        if (userDidConn.getInvitationId() == null)
            connectionStatusCode = -1;
        else{
            if (userDidConn.getStatus() == Status.CONNECTION_ESTABLISHED) {
                connectionStatusCode = 1;
            }
            else if(userDidConn.getStatus() == Status.INVITATION_GENERATED){
                if(userDidConn.getConnectionId() == null){
                    connectionStatusCode = 0;
                }
            }
        }
        // if (invitationID == null): return -1 (Invitation has never been generated)
        // else:
            // switch case:
                // case "status == connection_established": return 1
                // case "status == invitation_generated":
                    // call Aries /connections: [connection]
                    // if (connectionID == null): return 0 (Invitation has been generated but not yet accepted)
                    // else:
                        // call Aries /connections/{connectionID}/accept-request: null/empty
                        // call database updateDIDConnection(userID, myDID, theirDID, connectionID, status: connection_established): boolean
                        // return 1 (Connection has been established)

        return connectionStatusCode;
    }
}
