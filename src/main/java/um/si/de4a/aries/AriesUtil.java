package um.si.de4a.aries;

import com.google.gson.Gson;
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
import um.si.de4a.resources.offer.OfferRequest;

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

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            InputStream stream = urlConnection.getInputStream();

            String result = IOUtils.toString(stream, StandardCharsets.UTF_8.name());

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

            jsonInvitation = (JSONObject) jsonObject.get("invitation");
            System.out.println("[ARIES JSON invitation] " + jsonInvitation);

        } else {
            System.out.println("[ARIES JSON invitation] POST request has not worked");
        }
        urlConnection.disconnect();
        return jsonInvitation;
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

    public JSONObject signCredential(OfferRequest vcCredential) throws IOException, ParseException {
        JSONObject jsonSignedCredential = null;

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = null;

        Gson gson = new Gson();
        try {
            HttpPost request = new HttpPost(baseUrl + "verifiable/signcredential");
            StringEntity input = new StringEntity(gson.toJson(vcCredential));
            System.out.println("Request VC: " + gson.toJson(vcCredential));

            input.setContentType("application/json;charset=UTF-8");
            input.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
            request.setEntity(input);
            response = httpClient.execute(request);


            /*Checking response */
            if (response != null) {
                InputStream in = response.getEntity().getContent(); //Get the data in the entity
                String result = IOUtils.toString(in, StandardCharsets.UTF_8.name());

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

                jsonSignedCredential = (JSONObject) jsonObject.get("verifiableCredential");
                System.out.println("[ARIES result] " + jsonObject.toString());
            }
        }
        catch (Exception ex) {
            System.out.println("[ARIES sign-credential] Exception: " + ex.getMessage());
        } finally {
            httpClient.close();
        }

        /*HttpURLConnection urlConnection = (HttpURLConnection) new URL(baseUrl + "verifiable/signcredential").openConnection();

        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setDoOutput(true);
        try(OutputStream os = urlConnection.getOutputStream()) {
            byte[] input = vcCredential.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        urlConnection.connect();


        int responseCode = urlConnection.getResponseCode();
        System.out.println("[ARIES sign-credential] POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            InputStream stream = urlConnection.getInputStream();

            String result = IOUtils.toString(stream, StandardCharsets.UTF_8.name());

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

            jsonSignedCredential = (JSONObject) jsonObject.get("verifiableCredential");
            System.out.println("[ARIES result] " + jsonSignedCredential);

        } else {
            System.out.println("[ARIES sign-credential] POST request has not worked");
        }
        urlConnection.disconnect();
        */

        return jsonSignedCredential;
    }

    public String acceptRequest(String connectionId) throws IOException, ParseException {
        String response = "";
        String url = buildURL(baseUrl, connectionId, "accept-request");
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.connect();

        int responseCode = urlConnection.getResponseCode();
        System.out.println("[ARIES get-connections] GET Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            InputStream stream = urlConnection.getInputStream();

            String result = IOUtils.toString(stream, StandardCharsets.UTF_8.name());
            System.out.println("[result]: " + result);

        } else {
            System.out.println("[ARIES JSON connections] GET request has not worked");
        }
        urlConnection.disconnect();

        return response;
    }

    private String buildURL(String baseURL, String parameter, String method){
        return baseURL +  parameter + "/" + method;
    }
}
