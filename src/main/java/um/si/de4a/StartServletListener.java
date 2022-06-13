package um.si.de4a;

import javax.servlet.http.HttpServlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SuppressWarnings("serial")
public class StartServletListener extends HttpServlet {

    public void init() {
        System.out.println("=============================================");
        System.out.println("[DE4A] SSI Authority Agent is initializing.....");

        /*Gson gson = new Gson();
        Logger logger = null;
        try {
            logger = DE4ALogger.getLogger();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogRecord logRecordInfo = new LogRecord(Level.INFO, "");
        LogRecord logRecordSevere = new LogRecord(Level.SEVERE, "");


        System.out.println("Socket event: " + gson.toJson(socketEvent));

        SocketEvent event = new SocketEvent("did-exchange","userDidConn.getUserId()", "invitationID", 1);

        try {
            System.out.println("Going to push notification......");
            socketEvent.fire(event);

            logRecordInfo.setMessage("Event notification was successfully sent.");
            Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "01009"};
            logRecordInfo.setParameters(params);
            logger.log(logRecordInfo);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());

            logRecordSevere.setMessage("Event notification could not be sent.");
            Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "1010"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }*/
        /*SeContainerInitializer initializer = SeContainerInitializer.newInstance();
        try(SeContainer container = initializer.initialize()){
            EventNotifierResource eventNotifierResource = container.select(EventNotifierResource.class).get();

            eventNotifierResource.push(container);
        }
*/
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", "/usr/local/tomcat/webapps/DE4AEBSIConnector-0.1-launcher.jar");
        Process process = null;
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String s = "";
        while(true){
            try {
                if (!((s = in.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(s);
        }


    }
}
