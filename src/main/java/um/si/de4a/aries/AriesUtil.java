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
import um.si.de4a.resources.offer.OfferRequest;
import um.si.de4a.resources.offer.SignRequest;
import um.si.de4a.resources.vc.SendVCRequest;
import um.si.de4a.resources.vp.NamesObj;
import um.si.de4a.resources.vp.VPRequest;
import um.si.de4a.resources.vp.ValidateVCRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class AriesUtil {

    private AppConfig appConfig = null;
    private String baseUrl = "";

    public AriesUtil() throws IOException {
        appConfig = new AppConfig();
        baseUrl = appConfig.getProperties().getProperty("aries.enterprise.ip.address");
        System.out.println("[CONFIG] Aries IP address: " + baseUrl);
    }

    public JSONObject generateInvitation() throws IOException, ParseException {
        JSONObject jsonInvitation = null;
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(baseUrl + "connections/create-invitation?alias=de4a").openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.connect();
        int responseCode = urlConnection.getResponseCode();
        System.out.println("[ARIES generate-invitation] POST Response Code :: " + responseCode);

        JSONObject jsonObject = null;
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            InputStream stream = urlConnection.getInputStream();

            String result = IOUtils.toString(stream, StandardCharsets.UTF_8.name());

            JSONParser jsonParser = new JSONParser();
            jsonObject = (JSONObject) jsonParser.parse(result);

            //jsonInvitation = (JSONObject) jsonObject.get("invitation");
            System.out.println("[ARIES JSON invitation] " + jsonObject);

        } else {
            System.out.println("[ARIES JSON invitation] POST request has not worked");
        }
        urlConnection.disconnect();
        return jsonObject;
    }

    public ArrayList<JSONObject> getConnections() throws IOException, ParseException {
        ArrayList<JSONObject> connectionList = new ArrayList<>();
        JSONObject connection = null;

        HttpURLConnection urlConnection = (HttpURLConnection) new URL(baseUrl + "connections").openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        int responseCode = urlConnection.getResponseCode();
        System.out.println("[ARIES get-connections] GET Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            InputStream stream = urlConnection.getInputStream();

            String result = IOUtils.toString(stream, StandardCharsets.UTF_8.name());
            System.out.println("[result]: " + result);
            try {

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

                if (!jsonObject.isEmpty()) {
                    JSONArray resultsArray = (JSONArray) jsonObject.get("results");
                    System.out.println("[ARIES JSON connections] " + resultsArray.toString());

                    if (resultsArray.size() > 0) {
                        for (int i = 0; i < resultsArray.size(); i++) {
                            JSONObject connectionObj = (JSONObject) resultsArray.get(i);
                            connectionList.add(connectionObj);
                        }
                    }
                    System.out.println("[ARIES connectionList] Size: " + connectionList.size());
                }
            }catch(Exception ex){
                System.out.println("[ARIES get connections] Exception: " + ex.getMessage());
            }

        } else {
            System.out.println("[ARIES JSON connections] GET request has not worked");
        }
        urlConnection.disconnect();

        return connectionList;
    }

    public SignedVerifiableCredential signCredential(SignRequest vcCredential) throws IOException, ParseException {
        JSONObject jsonSignedCredential = null;

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = null;
        SignedVerifiableCredential signedVC = null;

        Gson gson = new Gson();
        try {
            HttpPost request = new HttpPost(baseUrl + "verifiable/signcredential");
            StringEntity input = new StringEntity(gson.toJson(vcCredential));
            System.out.println("[Sign-credential] Request VC: " + gson.toJson(vcCredential));

            input.setContentType("application/json;charset=UTF-8");
            input.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
            request.setEntity(input);
            response = httpClient.execute(request);
            System.out.println("Request: " + request.toString());
            if (response != null) {
                InputStream in = response.getEntity().getContent(); //Get the data in the entity
                String result = IOUtils.toString(in, StandardCharsets.UTF_8.name());

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

                jsonSignedCredential = (JSONObject) jsonObject.get("verifiableCredential");
                signedVC = gson.fromJson(gson.toJson(jsonSignedCredential), SignedVerifiableCredential.class);

                System.out.println("[ARIES result] " + jsonObject.toString());
            }
        }
        catch (Exception ex) {
            System.out.println("[ARIES sign-credential] Exception: " + ex.getMessage());
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
            System.out.println("Request offer: " + gson.toJson(offer));

            input.setContentType("application/json;charset=UTF-8");
            input.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
            request.setEntity(input);
            response = httpClient.execute(request);
            System.out.println("[Send offer] Request: " + request.toString());
            System.out.println("[Send offer] Response: " + response.toString());
            if (response != null) {
                InputStream in = response.getEntity().getContent(); //Get the data in the entity
                String result = IOUtils.toString(in, StandardCharsets.UTF_8.name());

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

                piid = jsonObject.get("piid").toString();

                System.out.println("[ARIES result] " + piid);
            }
        }
        catch (Exception ex) {
            System.out.println("[ARIES send-offer] Exception: " + ex.getMessage());
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
            System.out.println("[ARIES ACCEPT REQUEST] URL: " + url);

            HttpPost request = new HttpPost(url);
            StringEntity input = new StringEntity(gson.toJson(credential));
            System.out.println("[ACCEPT-REQUEST] Request credential: " + gson.toJson(credential));
            input.setContentType("application/json;charset=UTF-8");
            input.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
            request.setEntity(input);
            httpResponse = httpClient.execute(request);

            if (httpResponse != null) {
                InputStream in = httpResponse.getEntity().getContent(); //Get the data in the entity
                String result = IOUtils.toString(in, StandardCharsets.UTF_8.name());

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

                System.out.println("[ARIES result] " + jsonObject.toString());

                if (jsonObject.toString().equals("{}"))
                    response = true;
            }
        }
        catch (Exception ex) {
            System.out.println("[ARIES send-vc] Exception: " + ex.getMessage());
        } finally {
            httpClient.close();
        }

        return response;
    }

    public JSONObject getAction(String piid) throws IOException, ParseException {
        JSONObject action = null;

        HttpURLConnection urlConnection = (HttpURLConnection) new URL(baseUrl + "issuecredential/actions").openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        int responseCode = urlConnection.getResponseCode();
        System.out.println("[ARIES get-actions] GET Response Code :: " + responseCode);

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
                            if(actionObj.get("PIID").equals(piid)){
                                action = actionObj;
                                System.out.println("[ARIES actions] Found action match: " + action.get("PIID"));
                            }
                        }
                    }
                }
            }catch(Exception ex){
                System.out.println("[ARIES get actions] Exception: " + ex.getMessage());
            }

        } else {
            System.out.println("[ARIES JSON actions] GET request has not worked");
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
        httpResponse = httpClient.execute(request);

        System.out.println("[SEND-VP-REQUEST] Request:" + request.getEntity().getContent().toString());
        if (httpResponse != null) {
            InputStream in = httpResponse.getEntity().getContent(); //Get the data in the entity
            String result = IOUtils.toString(in, StandardCharsets.UTF_8.name());

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

            System.out.println("[ARIES result] " + jsonObject.toString());

            if (jsonObject.containsKey("piid")){
                piid = jsonObject.get("piid").toString();
            }

        }

        return piid;
    }

    public JSONObject getActionVP(String piid) throws IOException, ParseException {
        JSONObject action = null;

        HttpURLConnection urlConnection = (HttpURLConnection) new URL(baseUrl + "presentproof/actions").openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        int responseCode = urlConnection.getResponseCode();
        System.out.println("[ARIES get-actions] GET Response Code :: " + responseCode);

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
                            if(actionObj.get("PIID").equals(piid)){
                                action = actionObj;
                                System.out.println("[ARIES actions] Found action match: " + action.get("PIID"));
                            }
                        }
                    }
                }
            }catch(Exception ex){
                System.out.println("[ARIES get actions] Exception: " + ex.getMessage());
            }

        } else {
            System.out.println("[ARIES JSON actions] GET request has not worked");
        }
        urlConnection.disconnect();

        return action;
    }

    public boolean acceptPresentation(String piid, NamesObj namesObj) throws IOException {
        boolean response = false;

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse httpResponse = null;

        Gson gson = new Gson();


        System.out.println("[CHECK-REQUEST-VP-RESPONSE] Names: " + gson.toJson(namesObj));

        try {
            String url = buildURL(baseUrl, "presentproof", piid, "accept-presentation");
            System.out.println("[ARIES ACCEPT REQUEST] URL: " + url);

            HttpPost request = new HttpPost(url);
            StringEntity input = new StringEntity(gson.toJson(namesObj));
            input.setContentType("application/json;charset=UTF-8");
            input.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
            request.setEntity(input);
            httpResponse = httpClient.execute(request);

            if (httpResponse != null) {
                InputStream in = httpResponse.getEntity().getContent(); //Get the data in the entity
                String result = IOUtils.toString(in, StandardCharsets.UTF_8.name());

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

                System.out.println("[ARIES result] " + jsonObject.toString());

                if (jsonObject.toString().equals("{}"))
                    response = true;
            }
        }
        catch (Exception ex) {
            System.out.println("[ARIES send-vc] Exception: " + ex.getMessage());
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
        System.out.println("[ARIES get-presentations] GET Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            InputStream stream = urlConnection.getInputStream();

            String result = IOUtils.toString(stream, StandardCharsets.UTF_8.name());

            try {

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

                if (!jsonObject.isEmpty()) {
                    JSONArray resultsArray = (JSONArray) jsonObject.get("result");
                    System.out.println("JSON Array size: " + resultsArray.size());
                    if (resultsArray.size() > 0) {
                        for (int i = 0; i < resultsArray.size(); i++) {
                            JSONObject presentationObj = (JSONObject) resultsArray.get(i);
                            if(presentationObj.get("name").equals(name)){
                                presentation = presentationObj;
                                System.out.println("[ARIES presentations] Found VP match: " + presentation.get("id"));
                            }
                        }
                    }
                }
            }catch(Exception ex){
                System.out.println("[ARIES get presentations] Exception: " + ex.getMessage());
            }

        } else {
            System.out.println("[ARIES JSON presentations] GET request has not worked");
        }
        urlConnection.disconnect();

        return presentation;
    }

    public JSONObject findPresentationByID(String vpID) throws IOException {
        JSONObject presentation = null;

        HttpURLConnection urlConnection = (HttpURLConnection) new URL(baseUrl + "verifiable/presentation/" + vpID).openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        int responseCode = urlConnection.getResponseCode();
        System.out.println("[ARIES get-presentation] GET Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            InputStream stream = urlConnection.getInputStream();

            String result = IOUtils.toString(stream, StandardCharsets.UTF_8.name());

            try {

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

                if (!jsonObject.isEmpty()) {
                    JSONObject verifiablePresentationJSON = (JSONObject) jsonObject.get("verifiablePresentation");

                    if (verifiablePresentationJSON != null) {
                        System.out.println("[ARIES presentation] VP: " + verifiablePresentationJSON.get("id"));
                        presentation = verifiablePresentationJSON;
                    }
                }
            }catch(Exception ex){
                System.out.println("[ARIES get presentation] Exception: " + ex.getMessage());
            }

        } else {
            System.out.println("[ARIES JSON presentation] GET request has not worked");
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
            System.out.println("[ARIES VALIDATE VC] URL: " + url);

            HttpPost request = new HttpPost(url);
            StringEntity input = new StringEntity(gson.toJson(vcRequest));
            System.out.println("[VALIDATE-VC] Request data: " + gson.toJson(vcRequest));
            input.setContentType("application/json;charset=UTF-8");
            input.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
            request.setEntity(input);
            httpResponse = httpClient.execute(request);

            if (httpResponse != null) {
                InputStream in = httpResponse.getEntity().getContent(); //Get the data in the entity
                String result = IOUtils.toString(in, StandardCharsets.UTF_8.name());

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

                System.out.println("[ARIES result] " + jsonObject.toString());

                if (jsonObject.toString().equals("{}"))
                    response = true;
            }
        }
        catch (Exception ex) {
            System.out.println("[ARIES validate-vc] Exception: " + ex.getMessage());
        } finally {
            httpClient.close();
        }

        return response;
    }

    private String buildURL(String baseURL, String endpoint, String parameter, String method){
        return baseURL + endpoint + "/" + parameter + "/" + method;
    }


}
