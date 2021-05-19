package um.si.de4a.aries;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import um.si.de4a.AppConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
