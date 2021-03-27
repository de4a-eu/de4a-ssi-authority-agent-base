package um.si.de4a.resources.connection;


import javax.ws.rs.*;

@Path("/did-conn-status")
public class DIDConnStatusResource {

    @GET
    @Consumes("text/plain")
    @Produces("text/plain")
    public int fetch(@QueryParam("userId")String userID) {
        int connectionStatusCode = 0;

        //call database getDIDConn (userID): invitationID, connectionID, status

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
