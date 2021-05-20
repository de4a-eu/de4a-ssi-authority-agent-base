package um.si.de4a;

import java.io.*;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Scanner;

public class AppConfig {

    private Properties properties;

    public AppConfig() throws IOException {
        String propFile = new File("conf/app.properties").getAbsolutePath();

        FileInputStream inputStream = new FileInputStream(propFile);
        properties = new Properties();
        properties.load(inputStream);
        inputStream.close();
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
