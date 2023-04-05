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
package um.si.de4a.resources.vp;


import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import um.si.de4a.aries.AriesUtil;
import um.si.de4a.db.DBUtil;
import um.si.de4a.db.VPStatus;
import um.si.de4a.db.VPStatusEnum;

import javax.ws.rs.*;
import java.io.IOException;

@Path("/get-vp")
public class GetVPResource {

    @GET
    @Consumes("application/json")
    @Produces("application/json")
    @Path("{vpName}")
    public String getVP(@PathParam("vpName") String vpName) throws IOException {
        JSONObject vp = new JSONObject();
        AriesUtil ariesUtil = new AriesUtil();
        //System.out.println("[GET-VP] VPName: " + vpName);
        vp = ariesUtil.getPresentation(vpName);

        if(vp != null)
            return vp.toJSONString();
        return "";
    }
}
