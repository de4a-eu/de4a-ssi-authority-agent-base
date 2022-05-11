package um.si.de4a.resources.websocket;

import um.si.de4a.model.webhook.SocketEvent;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint("/websocket")
@SessionScoped
public class WSEndpoint implements Serializable {
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(final Session session) {
        try {
            session.getBasicRemote().sendText("You are connected. Your ID is " + session.getId());
            sessions.add(session);
        } catch (IOException ex) {
            Logger.getLogger(WSEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("WS End point class ID -- " + this.hashCode());
    }

    //@OnMessage
    //public void onMsg(@Observes(notifyObserver = Reception.IF_EXISTS) String msg){
    public void onMsg(@Observes SocketEvent msg) {

        //different WS enpoint instance - notice the hash code value in the server log
        System.out.println("WS endpoint received message: " + msg);
        /*try {
            for (Session s : sessions) {
                s.getBasicRemote().sendObject(msg);
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerEndpoint.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } catch (EncodeException e) {
            e.printStackTrace();
            Logger.getLogger(ServerEndpoint.class.getName()).log(Level.SEVERE, e.getMessage(), e);

        }*/
    }

    @OnClose
    public void onClose(final Session session) {
        try {
            session.getBasicRemote().sendText("WebSocket Session closed");
            sessions.remove(session);
        } catch (Exception ex) {
            Logger.getLogger(WSEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
