/*
 * Copyright (C) 2023, Partners of the EU funded DE4A project consortium
 *   (https://www.de4a.eu/consortium), under Grant Agreement No.870635
 * Author: University of Maribor (UM)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package um.si.de4a;

import java.io.*;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Scanner;

public class AppConfig {

    private Properties properties;

    public AppConfig() throws IOException {
        String propFile = new File("/usr/local/tomcat/conf/app.properties").getAbsolutePath();

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
