package um.si.de4a.aries;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.AppConfig;
import um.si.de4a.db.DBUtil;
import um.si.de4a.model.json.SignedVerifiableCredential;
import um.si.de4a.model.json.SignedVerifiableCredentialUpdated;
import um.si.de4a.resources.offer.OfferRequest;
import um.si.de4a.resources.offer.SignRequest;
import um.si.de4a.resources.vc.SendVCRequest;
import um.si.de4a.resources.vp.NamesObj;
import um.si.de4a.resources.vp.VPRequest;
import um.si.de4a.resources.vp.ValidateVCRequest;
import um.si.de4a.util.CustomDE4ALogFormatter;
import um.si.de4a.util.DE4ALogger;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class AriesUtil {

    private AppConfig appConfig = null;
    private String baseUrl = "", alias = "", invitationURL = "";
    private Logger logger = null;
    private LogRecord logRecordInfo = null;
    private LogRecord logRecordSevere = null;

    public AriesUtil() throws IOException {
        logger = DE4ALogger.getLogger();
        logRecordInfo = new LogRecord(Level.INFO, "");
        logRecordSevere = new LogRecord(Level.SEVERE, "");

        appConfig = new AppConfig();
        try {
            baseUrl = appConfig.getProperties().getProperty("aries.enterprise.ip.address");
            alias = appConfig.getProperties().getProperty("alias");
            invitationURL = appConfig.getProperties().getProperty("organization.img.url");
        }
        catch(Exception ex){
            logRecordSevere.setMessage( "Error reading configuration properties.");
            Object[] params = new Object[]{"Authority Agent DT", "Authority Agent DT", "3001"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }

    }

    public JSONObject generateInvitation() throws IOException, ParseException {
        JSONObject jsonInvitation = null;
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(baseUrl + "connections/create-invitation?alias=" + alias).openConnection();
        urlConnection.setRequestMethod("POST");
        try {
            urlConnection.connect();
        }
        catch (Exception ex){
            logRecordSevere.setMessage( "GENERATE-INVITATION: Connection error with Aries Government Agent.");
            Object[] params = new Object[]{"Authority Agent DT", "Aries Government Agent", "10703"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }

        int responseCode = urlConnection.getResponseCode();

        logRecordInfo.setMessage("GENERATE-INVITATION: Received HTTP response code " + responseCode + " from endpoint: " + urlConnection.getURL().toString());
        Object[] params = new Object[]{"Authority Agent DT", "Aries Government Agent", "0101"};
        logRecordInfo.setParameters(params);
        logger.log(logRecordInfo);

        JSONObject jsonObject = null;
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            InputStream stream = urlConnection.getInputStream();

            String result = IOUtils.toString(stream, StandardCharsets.UTF_8.name());

            JSONParser jsonParser = new JSONParser();
            jsonObject = (JSONObject) jsonParser.parse(result);

            if(jsonObject.containsKey("invitation_url"))
                jsonObject.put("invitation_url", invitationURL);
            //jsonInvitation = (JSONObject) jsonObject.get("invitation");
            logRecordInfo.setMessage("GENERATE-INVITATION: Generated DID invitation for edge agent.");
            params = new Object[]{"Authority Agent DT", "Evidence portal DO", "0203"};
            logRecordInfo.setParameters(params);
            logger.log(logRecordInfo);
        } else {
            logRecordSevere.setMessage( "GENERATE-INVITATION: Error on response from the Aries Government Agent.");
            params = new Object[]{"Authority Agent DT", "Aries Government Agent", "10704"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }
        urlConnection.disconnect();
        return jsonObject;
    }

    public ArrayList<JSONObject> getConnections() throws IOException, ParseException {
        ArrayList<JSONObject> connectionList = new ArrayList<>();
        JSONObject connection = null;

        HttpURLConnection urlConnection = (HttpURLConnection) new URL(baseUrl + "connections").openConnection();
        urlConnection.setRequestMethod("GET");
        try {
            urlConnection.connect();
        }
        catch(Exception ex){
            logRecordSevere.setMessage( "DID-CONN-STATUS: Connection error with Aries Government Agent.");
            Object[] params = new Object[]{"Authority Agent DT", "Aries Government Agent", "10703"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }

        int responseCode = urlConnection.getResponseCode();

        logRecordInfo.setMessage("DID-CONN-STATUS: Received HTTP response code " + responseCode + " from endpoint: " + urlConnection.getURL().toString());
        Object[] params = new Object[]{"Authority Agent DT", "Aries Government Agent", "0101"};
        logRecordInfo.setParameters(params);
        logger.log(logRecordInfo);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            InputStream stream = urlConnection.getInputStream();

            String result = IOUtils.toString(stream, StandardCharsets.UTF_8.name());
            try {

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

                if (!jsonObject.isEmpty()) {
                    JSONArray resultsArray = (JSONArray) jsonObject.get("results");

                    if (resultsArray.size() > 0) {
                        for (int i = 0; i < resultsArray.size(); i++) {
                            JSONObject connectionObj = (JSONObject) resultsArray.get(i);
                            connectionList.add(connectionObj);
                            logRecordInfo.setMessage("DID-CONN-STATUS: Processing the JSON response received from /connections.");
                            params = new Object[]{"Authority Agent DT", "Evidence portal DO", "0102"};
                            logRecordInfo.setParameters(params);
                            logger.log(logRecordInfo);
                        }
                    }
                    //System.out.println("[ARIES connectionList] Size: " + connectionList.size());
                }
            }catch(Exception ex){
                logRecordSevere.setMessage( "DID-CONN-STATUS: Error on response from the Aries Government Agent.");
                params = new Object[]{"Authority Agent DT", "Aries Government Agent", "10704"};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
                //System.out.println("[ARIES get connections] Exception: " + ex.getMessage());
            }

        } else {
            logRecordSevere.setMessage( "DID-CONN-STATUS: Connection error with Aries Government Agent.");
            params = new Object[]{"Authority Agent DT", "Aries Government Agent", "10703"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }
        urlConnection.disconnect();

        return connectionList;
    }

    public SignedVerifiableCredentialUpdated signCredential(SignRequest vcCredential) throws IOException, ParseException {
        JSONObject jsonSignedCredential = null;

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = null;
        SignedVerifiableCredentialUpdated signedVC = null;

        Gson gson = new Gson();
        try {
            HttpPost request = new HttpPost(baseUrl + "verifiable/signcredential");
            StringEntity input = new StringEntity(gson.toJson(vcCredential));

            input.setContentType("application/json;charset=UTF-8");
            input.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
            request.setEntity(input);

            try {
                response = httpClient.execute(request);
                logRecordInfo.setMessage("SEND-OFFER: Received HTTP response code " + response.getStatusLine().getStatusCode() + " from endpoint: " + baseUrl + "verifiable/signcredential");
                Object[] params = new Object[]{"Authority Agent DT", "Aries Government Agent", "0101"};
                logRecordInfo.setParameters(params);
                logger.log(logRecordInfo);
            }
            catch(Exception ex){
                logRecordSevere.setMessage( "SEND-OFFER: Connection error with Aries Government Agent.");
                Object[] params = new Object[]{"Authority Agent DT", "Aries Government Agent", "10703"};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }

            if (response != null) {
                InputStream in = response.getEntity().getContent(); //Get the data in the entity
                String result = IOUtils.toString(in, StandardCharsets.UTF_8.name());

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
                try {
                    jsonSignedCredential = (JSONObject) jsonObject.get("verifiableCredential");
                    logRecordInfo.setMessage("SEND-OFFER: Processing the JSON response received from /sign-credential.");
                    Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "0102"};
                    logRecordInfo.setParameters(params);
                    logger.log(logRecordInfo);
                }
                catch (Exception ex){
                    logRecordSevere.setMessage( "SEND-OFFER: Error on response from the Aries Government Agent.");
                    Object[] params = new Object[]{"Authority Agent DT", "Aries Government Agent", "10704"};
                    logRecordSevere.setParameters(params);
                    logger.log(logRecordSevere);
                }
                try {
                    signedVC = gson.fromJson(gson.toJson(jsonSignedCredential), SignedVerifiableCredentialUpdated.class);

                    logRecordInfo.setMessage("SEND-OFFER: Signed a Verifiable Credential." + signedVC.getId());
                    Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "0205"};
                    logRecordInfo.setParameters(params);
                    logger.log(logRecordInfo);
                }
                catch(Exception ex){
                    logRecordSevere.setMessage("SEND-OFFER: Object conversion error on Authority Agent DT.");
                    Object[] params = new Object[]{"Authority Agent DT", "Evidence portal DO", "20005"};
                    logRecordSevere.setParameters(params);
                    logger.log(logRecordSevere);
                }
                //System.out.println("[ARIES result] " + jsonObject.toString());
            }
        }
        catch(Exception ex) {
            //System.out.println("[ARIES sign-credential] Exception: " + ex.getMessage());
            logRecordSevere.setMessage( "SEND-OFFER: Connection error with Aries Government Agent.");
            Object[] params = new Object[]{"Authority Agent DT", "Aries Government Agent", "10703"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        } finally {
            httpClient.close();
        }

        return signedVC;
    }

    public String sendOffer(OfferRequest offer) throws IOException, ParseException {

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = null;
        String piid = "";

        Gson gson = new Gson();
        try {
            HttpPost request = new HttpPost(baseUrl + "issuecredential/send-offer");
            StringEntity input = new StringEntity(gson.toJson(offer));
            //System.out.println("Request offer: " + gson.toJson(offer));

            input.setContentType("application/json;charset=UTF-8");
            input.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
            request.setEntity(input);
            try {
                response = httpClient.execute(request);
                logRecordInfo.setMessage("SEND-OFFER: Received HTTP response code " + response.getStatusLine().getStatusCode() + " from endpoint: " + baseUrl + "issuecredential/send-offer");
                Object[] params = new Object[]{"Authority Agent DT", "Aries Government Agent", "0101"};
                logRecordInfo.setParameters(params);
                logger.log(logRecordInfo);
            }
            catch(Exception ex){
                logRecordSevere.setMessage( "SEND-OFFER: Connection error with Aries Government Agent.");
                Object[] params = new Object[]{"Authority Agent DT", "Aries Government Agent", "10703"};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }

            if (response != null) {
                InputStream in = response.getEntity().getContent(); //Get the data in the entity
                String result = IOUtils.toString(in, StandardCharsets.UTF_8.name());

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
                try {
                    piid = jsonObject.get("piid").toString();

                    logRecordInfo.setMessage("SEND-OFFER: Received response data PIID: " + piid);
                    Object[] params = new Object[]{"Authority Agent DT",  "Aries Government Agent", "0106"};
                    logRecordInfo.setParameters(params);
                    logger.log(logRecordInfo);
                }
                catch(Exception ex){
                    logRecordSevere.setMessage( "SEND-OFFER: Error on response from the Aries Government Agent.");
                    Object[] params = new Object[]{"Authority Agent DT", "Aries Government Agent", "10704"};
                    logRecordSevere.setParameters(params);
                    logger.log(logRecordSevere);
                }
            }
        }
        catch (Exception ex) {
            logRecordSevere.setMessage( "SEND-OFFER: Connection error with Aries Government Agent.");
            Object[] params = new Object[]{"Authority Agent DT", "Aries Government Agent", "10703"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        } finally {
            httpClient.close();
        }

        return piid;
    }


    public boolean acceptRequest(String piid, SendVCRequest credential) throws IOException, ParseException {
        boolean response = false;

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse httpResponse = null;

        Gson gson = new Gson();
        try {
            String url = buildURL(baseUrl, "issuecredential", piid, "accept-request");

            HttpPost request = new HttpPost(url);
            StringEntity input = new StringEntity(gson.toJson(credential));
            //System.out.println("[ACCEPT-REQUEST] Request credential: " + gson.toJson(credential));
            input.setContentType("application/json;charset=UTF-8");
            input.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
            request.setEntity(input);
            try {
                httpResponse = httpClient.execute(request);

                logRecordInfo.setMessage("SEND-VC: Received HTTP response code " + httpResponse.getStatusLine().getStatusCode() + " from endpoint: " + baseUrl + "issuecredential/accept-request");
                Object[] params = new Object[]{"Authority Agent DT", "Aries Government Agent", "0101"};
                logRecordInfo.setParameters(params);
                logger.log(logRecordInfo);
            }
            catch(Exception ex){
                logRecordSevere.setMessage( "SEND-VC: Connection error with Aries Government Agent.");
                Object[] params = new Object[]{"Authority Agent DT", "Aries Government Agent", "10703"};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }

            if (httpResponse != null) {
                InputStream in = httpResponse.getEntity().getContent(); //Get the data in the entity
                String result = IOUtils.toString(in, StandardCharsets.UTF_8.name());

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = null;
                try {
                    jsonObject = (JSONObject) jsonParser.parse(result);
                }
                catch(Exception ex){
                    logRecordSevere.setMessage( "SEND-VC: Error on response from the Aries Government Agent.");
                    Object[] params = new Object[]{"Authority Agent DT", "Aries Government Agent", "10704"};
                    logRecordSevere.setParameters(params);
                    logger.log(logRecordSevere);
                }
                //System.out.println("[ARIES result] " + jsonObject.toString());

                if (jsonObject.toString().equals("{}")) {
                    logRecordInfo.setMessage("SEND-VC: Sent a Verifiable Credential to the edge agent.");
                    Object[] params = new Object[]{"Authority Agent DT",  "Evidence Portal DT", "0206"};
                    logRecordInfo.setParameters(params);
                    logger.log(logRecordInfo);
                    response = true;
                }
            }
        }
        catch (Exception ex) {
            logRecordSevere.setMessage( "SEND-VC: Connection error with Aries Government Agent.");
            Object[] params = new Object[]{"Authority Agent DR", "Aries Government Agent", "10703"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
            //System.out.println("[ARIES send-vc] Exception: " + ex.getMessage());
        } finally {
            httpClient.close();
        }

        return response;
    }

    public JSONObject getAction(String piid) throws IOException, ParseException {
        JSONObject action = null;

        HttpURLConnection urlConnection = (HttpURLConnection) new URL(baseUrl + "issuecredential/actions").openConnection();
        urlConnection.setRequestMethod("GET");
        try {
            urlConnection.connect();
        }
        catch(Exception ex){
            logRecordSevere.setMessage( "CHECK-OFFER-VC-STATUS: Connection error with Aries Government Agent.");
            Object[] params = new Object[]{"Authority Agent DT", "Aries Government Agent", "10703"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }

        int responseCode = urlConnection.getResponseCode();

        logRecordInfo.setMessage("CHECK-OFFER-VC-STATUS: Received HTTP response code " + responseCode + " from endpoint: " + urlConnection.getURL().toString());
        Object[] params = new Object[]{"Authority Agent DT", "Aries Government Agent", "0101"};
        logRecordInfo.setParameters(params);
        logger.log(logRecordInfo);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            InputStream stream = urlConnection.getInputStream();

            String result = IOUtils.toString(stream, StandardCharsets.UTF_8.name());

            try {

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

                if (!jsonObject.isEmpty()) {
                    JSONArray resultsArray = (JSONArray) jsonObject.get("actions");

                    if (resultsArray.size() > 0) {
                        for (int i = 0; i < resultsArray.size(); i++) {
                            JSONObject actionObj = (JSONObject) resultsArray.get(i);
                            logRecordInfo.setMessage("CHECK-OFFER-VC-STATUS: Processing the JSON response received from /issuecredential/actions.");
                            params = new Object[]{"Authority Agent DT", "Evidence portal DO", "0102"};
                            logRecordInfo.setParameters(params);
                            logger.log(logRecordInfo);

                            if(actionObj.get("PIID").equals(piid)){
                                action = actionObj;
                                logRecordInfo.setMessage("CHECK-OFFER-VC-STATUS: Found a VC action match with PIID: " + piid);
                                params = new Object[]{"Authority Agent DT",  "Evidence Portal DT", "0107"};
                                logRecordInfo.setParameters(params);
                                logger.log(logRecordInfo);
                            }
                        }
                    }
                }
            }catch(Exception ex){
                logRecordSevere.setMessage( "CHECK-OFFER-VC-STATUS: Error on response from the Aries Government Agent.");
                params = new Object[]{"Authority Agent DT", "Aries Government Agent", "10704"};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }

        } else {
            logRecordSevere.setMessage( "CHECK-OFFER-VC-STATUS: Connection error with Aries Government Agent.");
            params = new Object[]{"Authority Agent DT", "Aries Government Agent", "10703"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }
        urlConnection.disconnect();

        return action;
    }

    public String sendRequest(VPRequest vpRequest) throws IOException, ParseException {
        String piid = "";
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse httpResponse = null;

        Gson gson = new Gson();
        HttpPost request = new HttpPost(baseUrl + "presentproof/send-request-presentation");
        StringEntity input = new StringEntity(gson.toJson(vpRequest));
        input.setContentType("application/json;charset=UTF-8");
        input.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
        request.setEntity(input);

        try {
            httpResponse = httpClient.execute(request);

            logRecordInfo.setMessage("SEND-VP-REQUEST: Received HTTP response code " + httpResponse.getStatusLine().getStatusCode() + " from endpoint: " + baseUrl + "presentproof/send-request-presentation");
            Object[] params = new Object[]{"Authority Agent DR", "Aries Government Agent", "0101"};
            logRecordInfo.setParameters(params);
            logger.log(logRecordInfo);
        }
        catch(Exception ex){
            logRecordSevere.setMessage( "SEND-VP-REQUEST: Connection error with Aries Government Agent.");
            Object[] params = new Object[]{"Authority Agent DR", "Aries Government Agent", "10703"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }

        //System.out.println("[SEND-VP-REQUEST] Request:" + request.getEntity().getContent().toString());
        if (httpResponse != null) {
            InputStream in = httpResponse.getEntity().getContent(); //Get the data in the entity
            String result = IOUtils.toString(in, StandardCharsets.UTF_8.name());

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = null;
            try{
                jsonObject = (JSONObject) jsonParser.parse(result);
            }
            catch(Exception ex){
                logRecordSevere.setMessage( "Error on response from the Aries Government Agent.");
                Object[] params = new Object[]{"Authority Agent DR", "Aries Government Agent", "1003"};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }
            //System.out.println("[ARIES result] " + jsonObject.toString());

            if (jsonObject.containsKey("piid")){
                try {
                    piid = jsonObject.get("piid").toString();
                    logRecordInfo.setMessage("SEND-VP-REQUEST: Received response data PIID: " + piid);
                    Object[] params = new Object[]{"Authority Agent DR",  "Aries Government Agent", "0106"};
                    logRecordInfo.setParameters(params);
                    logger.log(logRecordInfo);
                }
                catch(Exception ex){
                    logRecordSevere.setMessage( "SEND-VP-REQUEST: Error on response from the Aries Government Agent.");
                    Object[] params = new Object[]{"Authority Agent DR", "Aries Government Agent", "10704"};
                    logRecordSevere.setParameters(params);
                    logger.log(logRecordSevere);
                }
            }

        }
        else{
            logRecordSevere.setMessage( "SEND-VP-REQUEST: Connection error with Aries Government Agent.");
            Object[] params = new Object[]{"Authority Agent DR", "Aries Government Agent", "10703"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }

        return piid;
    }

    public JSONObject getActionVP(String piid) throws IOException, ParseException {
        JSONObject action = null;

        HttpURLConnection urlConnection = (HttpURLConnection) new URL(baseUrl + "presentproof/actions").openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        int responseCode = urlConnection.getResponseCode();

        logRecordInfo.setMessage("CHECK-REQUEST-VP-STATUS: Received HTTP response code " + responseCode + " from endpoint: " + urlConnection.getURL().toString());
        Object[] params = new Object[]{"Authority Agent DR", "Aries Government Agent", "0101"};
        logRecordInfo.setParameters(params);
        logger.log(logRecordInfo);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            InputStream stream = urlConnection.getInputStream();

            String result = IOUtils.toString(stream, StandardCharsets.UTF_8.name());

            try {
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

                if (!jsonObject.isEmpty()) {
                    JSONArray resultsArray = (JSONArray) jsonObject.get("actions");

                    if (resultsArray.size() > 0) {
                        for (int i = 0; i < resultsArray.size(); i++) {
                            JSONObject actionObj = (JSONObject) resultsArray.get(i);

                            logRecordInfo.setMessage("CHECK-REQUEST-VP-STATUS: Processing the JSON response received from /presentproof/actions.");
                            params = new Object[]{"Authority Agent DR",  "eProcedure Portal DE", "0102"};
                            logRecordInfo.setParameters(params);
                            logger.log(logRecordInfo);

                            if(actionObj.get("PIID").equals(piid)){
                                action = actionObj;
                                logRecordInfo.setMessage("CHECK-REQUEST-VP-STATUS: Found a VP action match with PIID: " + piid);
                                params = new Object[]{"Authority Agent DR",  "eProcedure Portal DE", "0107"};
                                logRecordInfo.setParameters(params);
                                logger.log(logRecordInfo);
                                //System.out.println("[ARIES actions] Found action match: " + action.get("PIID"));
                            }
                        }
                    }
                }
            }catch(Exception ex){
                logRecordSevere.setMessage( "CHECK-REQUEST-VP-STATUS: Error on response from the Aries Government Agent.");
                params = new Object[]{"Authority Agent DR", "Aries Government Agent", "10704"};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
                //System.out.println("[ARIES get actions] Exception: " + ex.getMessage());
            }

        } else {
            logRecordSevere.setMessage( "CHECK-REQUEST-VP-STATUS: Connection error with Aries Government Agent.");
            params = new Object[]{"Authority Agent DR", "Aries Government Agent", "10703"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
            // System.out.println("[ARIES JSON actions] GET request has not worked");
        }
        urlConnection.disconnect();

        return action;
    }

    public boolean acceptPresentation(String piid, NamesObj namesObj) throws IOException {
        boolean response = false;

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse httpResponse = null;

        Gson gson = new Gson();

        try {
            String url = buildURL(baseUrl, "presentproof", piid, "accept-presentation");

            HttpPost request = new HttpPost(url);
            StringEntity input = new StringEntity(gson.toJson(namesObj));
            input.setContentType("application/json;charset=UTF-8");
            input.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
            request.setEntity(input);

            try {
                httpResponse = httpClient.execute(request);

                logRecordInfo.setMessage("CHECK-REQUEST-VP-STATUS: Received HTTP response code " + httpResponse.getStatusLine().getStatusCode() + " from endpoint: " + baseUrl + "presentproof/accept-presentation");
                Object[] params = new Object[]{"Authority Agent DR", "Aries Government Agent", "0101"};
                logRecordInfo.setParameters(params);
                logger.log(logRecordInfo);
            }
            catch(Exception ex){
                logRecordSevere.setMessage( "CHECK-REQUEST-VP-STATUS: Connection error with Aries Government Agent.");
                Object[] params = new Object[]{"Authority Agent DR", "Aries Government Agent", "10703"};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }

            if (httpResponse != null) {
                InputStream in = httpResponse.getEntity().getContent(); //Get the data in the entity
                String result = IOUtils.toString(in, StandardCharsets.UTF_8.name());

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

                //System.out.println("[ARIES result] " + jsonObject.toString());

                if (jsonObject.toString().equals("{}")) {
                    logRecordInfo.setMessage("CHECK-REQUEST-VP-STATUS: Accepted a submitted Verifiable Presentation.");
                    Object[] params = new Object[]{"Authority Agent DR",  "eProcedure Portal DE", "0207"};
                    logRecordInfo.setParameters(params);
                    logger.log(logRecordInfo);
                    response = true;
                }
            }
        }
        catch (Exception ex) {
            logRecordSevere.setMessage( "CHECK-REQUEST-VP-STATUS: Error on response from the Aries Government Agent.");
            Object[] params = new Object[]{"Authority Agent DR", "Aries Government Agent", "10704"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);

        } finally {
            httpClient.close();
        }

        return response;
    }

    public JSONObject getPresentation(String name) throws IOException {
        JSONObject presentation = null;

        HttpURLConnection urlConnection = (HttpURLConnection) new URL(baseUrl + "verifiable/presentations").openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        int responseCode = urlConnection.getResponseCode();

        logRecordInfo.setMessage("GET-VP: Received HTTP response code " + responseCode + " from endpoint: " + urlConnection.getURL().toString());
        Object[] params = new Object[]{"Authority Agent DR", "Aries Government Agent", "0101"};
        logRecordInfo.setParameters(params);
        logger.log(logRecordInfo);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            InputStream stream = urlConnection.getInputStream();

            String result = IOUtils.toString(stream, StandardCharsets.UTF_8.name());

            try {

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

                if (!jsonObject.isEmpty()) {
                    JSONArray resultsArray = null;
                    try{
                        resultsArray = (JSONArray) jsonObject.get("result");

                        logRecordInfo.setMessage("GET-VP: Processing the JSON response received from /verifiable/presentations.");
                        params = new Object[]{"Authority Agent DR",  "eProcedure Portal DE", "0102"};
                        logRecordInfo.setParameters(params);
                        logger.log(logRecordInfo);

                        if (resultsArray.size() > 0) {
                            for (int i = 0; i < resultsArray.size(); i++) {
                                JSONObject presentationObj = (JSONObject) resultsArray.get(i);
                                if(presentationObj.get("name").equals(name)){
                                    presentation = presentationObj;

                                    logRecordInfo.setMessage("GET-VP: Found a Verifiable Presentation with name: " + name);
                                    params = new Object[]{"Authority Agent DR",  "eProcedure Portal DE", "0108"};
                                    logRecordInfo.setParameters(params);
                                    logger.log(logRecordInfo);
                                }
                            }
                        }
                    }
                    catch(Exception ex){
                        logRecordSevere.setMessage( "GET-VP: Error on response from the Aries Government Agent.");
                        params = new Object[]{"Authority Agent DR", "Aries Government Agent", "10704"};
                        logRecordSevere.setParameters(params);
                        logger.log(logRecordSevere);
                    }
                }
            }catch(Exception ex){
                logRecordSevere.setMessage( "GET-VP: Error on response from the Aries Government Agent.");
                params = new Object[]{"Authority Agent DR", "Aries Government Agent", "10704"};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }

        } else {
            logRecordSevere.setMessage( "GET-VP: Connection error with Aries Government Agent.");
            params = new Object[]{"Authority Agent DR", "Aries Government Agent", "10703"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }
        urlConnection.disconnect();

        JSONObject vpContent = findPresentationByID(Base64.getEncoder().encodeToString(presentation.get("id").toString().getBytes(StandardCharsets.UTF_8)));
        return vpContent;
    }

    public JSONObject findPresentationByID(String vpID) throws IOException {
        JSONObject presentation = null;

        HttpURLConnection urlConnection = (HttpURLConnection) new URL(baseUrl + "verifiable/presentation/" + vpID).openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        int responseCode = urlConnection.getResponseCode();
        logRecordInfo.setMessage("GET-VP: Received HTTP response code " + responseCode + " from endpoint: " + urlConnection.getURL().toString());
        Object[] params = new Object[]{"Authority Agent DR", "Aries Government Agent", "0101"};
        logRecordInfo.setParameters(params);
        logger.log(logRecordInfo);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            InputStream stream = urlConnection.getInputStream();

            String result = IOUtils.toString(stream, StandardCharsets.UTF_8.name());

            try {

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

                if (!jsonObject.isEmpty()) {
                    JSONObject verifiablePresentationJSON = null;
                    try{
                        verifiablePresentationJSON = (JSONObject) jsonObject.get("verifiablePresentation");

                        logRecordInfo.setMessage("GET-VP: Processing the JSON response received from /verifiable/presentation/" + vpID + ".");
                        params = new Object[]{"Authority Agent DR",  "eProcedure Portal DE", "0102"};
                        logRecordInfo.setParameters(params);
                        logger.log(logRecordInfo);
                    }
                    catch(Exception ex){
                        logRecordSevere.setMessage( "GET-VP: Error on response from the Aries Government Agent.");
                        params = new Object[]{"Authority Agent DR", "Aries Government Agent", "10704"};
                        logRecordSevere.setParameters(params);
                        logger.log(logRecordSevere);
                    }

                    if (verifiablePresentationJSON != null) {
                        presentation = verifiablePresentationJSON;
                        logRecordInfo.setMessage("GET-VP: Found a Verifiable Presentation with ID: " + vpID);
                        params = new Object[]{"Authority Agent DR",  "eProcedure Portal DE", "0109"};
                        logRecordInfo.setParameters(params);
                        logger.log(logRecordInfo);
                    }
                }
            }catch(Exception ex){
                logRecordSevere.setMessage( "GET-VP: Error on response from the Aries Government Agent.");
                params = new Object[]{"Authority Agent DR", "Aries Government Agent", "10704"};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }

        } else {
            logRecordSevere.setMessage( "GET-VP: Connection error with Aries Government Agent.");
            params = new Object[]{"Authority Agent DR", "Aries Government Agent", "10703"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        }
        urlConnection.disconnect();

        return presentation;
    }

    public boolean validateVCProof(ValidateVCRequest vcRequest) throws IOException, ParseException {
        boolean response = false;

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse httpResponse = null;

        Gson gson = new Gson();
        try {
            String url = baseUrl + "verifiable/credential/validate";

            HttpPost request = new HttpPost(url);
            StringEntity input = new StringEntity(gson.toJson(vcRequest));
            //System.out.println("[VALIDATE-VC] Request data: " + gson.toJson(vcRequest));
            input.setContentType("application/json;charset=UTF-8");
            input.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
            request.setEntity(input);
            try {
                httpResponse = httpClient.execute(request);

                logRecordInfo.setMessage("VALIDATE-VP: Received HTTP response code " + httpResponse.getStatusLine().getStatusCode() + " from endpoint: " + url + ".");
                Object[] params = new Object[]{"Authority Agent DR", "Aries Government Agent", "0101"};
                logRecordInfo.setParameters(params);
                logger.log(logRecordInfo);
            }
            catch(Exception ex){
                logRecordSevere.setMessage( "VALIDATE-VP: Connection error with Aries Government Agent.");
                Object[] params = new Object[]{"Authority Agent DR", "Aries Government Agent", "10703"};
                logRecordSevere.setParameters(params);
                logger.log(logRecordSevere);
            }

            if (httpResponse != null) {
                InputStream in = httpResponse.getEntity().getContent(); //Get the data in the entity
                String result = IOUtils.toString(in, StandardCharsets.UTF_8.name());

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

                //System.out.println("[ARIES result] " + jsonObject.toString());

                if (jsonObject.toString().equals("{}")) {
                    response = true;
                }
            }
        }
        catch (Exception ex) {
            logRecordSevere.setMessage( "VALIDATE-VP: Error on response from the Aries Government Agent.");
            Object[] params = new Object[]{"Authority Agent DR", "Aries Government Agent", "10704"};
            logRecordSevere.setParameters(params);
            logger.log(logRecordSevere);
        } finally {
            httpClient.close();
        }

        return response;
    }

    private String buildURL(String baseURL, String endpoint, String parameter, String method){
        return baseURL + endpoint + "/" + parameter + "/" + method;
    }


}
