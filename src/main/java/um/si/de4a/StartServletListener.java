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

import javax.servlet.http.HttpServlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SuppressWarnings("serial")
public class StartServletListener extends HttpServlet {

    public void init() {
        System.out.println("=============================================");
        System.out.println("[DE4A] SSI Authority Agent is initializing.....");

        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", "/usr/local/tomcat/webapps/DE4AEBSIConnector-2.1-launcher.jar");
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
