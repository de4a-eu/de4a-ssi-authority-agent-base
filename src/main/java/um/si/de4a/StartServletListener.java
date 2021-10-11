package um.si.de4a;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@SuppressWarnings("serial")
public class StartServletListener extends HttpServlet {

    public void init() {
        System.out.println("=============================================");
        System.out.println("[DE4A] SSI Authority Agent is initializing.....");

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
