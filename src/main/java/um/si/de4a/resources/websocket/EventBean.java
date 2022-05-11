package um.si.de4a.resources.websocket;

import jnr.constants.platform.Sock;
import um.si.de4a.model.webhook.SocketEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.Serializable;

@Named
@SessionScoped
public class EventBean implements Serializable {

    @Inject
    transient private Event<SocketEvent> event;

    public EventBean() {
    }

    @Inject
    public EventBean(Event<SocketEvent> event) {
        System.out.println("storing event bean....");
        this.event = event;
        System.out.println("Event: " + this.getEvent());
    }

    public void sendMsg(String s){
        System.out.println("hello " + s);
    }

    public Event<SocketEvent> getEvent() {
        return event;
    }

    public void setEvent(Event<SocketEvent> event) {
        this.event = event;
    }
}
