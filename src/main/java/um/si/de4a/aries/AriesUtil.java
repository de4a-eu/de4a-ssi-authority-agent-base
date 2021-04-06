package um.si.de4a.aries;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AriesUtil {

    private String result;

    public AriesUtil() {
        result = "";
    }

    public JSONObject generateInvitation() throws IOException, ParseException {
        JSONObject jsonInvitation = null;

        HttpURLConnection urlConnection = (HttpURLConnection) new URL("http://164.8.250.43:9082/connections/create-invitation?alias=de4a").openConnection();
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

}
