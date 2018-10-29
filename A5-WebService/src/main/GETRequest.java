/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author HC Finnson
 */
public class GETRequest {
    public static void main(String[] args) {
        GETRequest request = new GETRequest("104.248.47.74", 80);
        request.doRequestGet();
    }

    private String BASE_URL; // Base URL (address) of the server

    /**
     * Create an HTTP GET request
     *
     * @param host Will send request to this host: IP address or domain
     * @param port Will use this port
     */
    public GETRequest(String host, int port) {
        BASE_URL = "http://" + host + ":" + port + "/";
    }

    /**
     * Send an HTTP GET to a specific path on the web server
     */
    public void doRequestGet() {
        // TODO: change path to something correct
        sendGet("dkrest/test/get2");
    }

    /**
     * Send HTTP GET
     *
     * @param path     Relative path in the API.
     */
    private void sendGet(String path) {
        try {
            String url = BASE_URL + path;
            URL urlObj = new URL(url);
            System.out.println("Sending HTTP GET to " + url);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Server reached");
                // Response was OK, read the body (data)
                InputStream stream = con.getInputStream();
                String responseBody = convertStreamToString(stream);
                stream.close();
                System.out.println("Response from the server:");
                System.out.println(responseBody);
                parseJSONObject(responseBody);
            } else {
                String responseDescription = con.getResponseMessage();
                System.out.println("Request failed, response code: " + responseCode + " (" + responseDescription + ")");
            }
        } catch (ProtocolException e) {
            System.out.println("Protocol not supported by the server");
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Read the whole content from an InputStream, return it as a string
     * @param is Inputstream to read the body from
     * @return The whole body as a string
     */
    private String convertStreamToString(InputStream is) {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
                response.append('\n');
            }
        } catch (IOException ex) {
            System.out.println("Could not read the data from HTTP response: " + ex.getMessage());
        }
        return response.toString();
    }
    
    private void parseJSONObject(String jsonObjectString) {
        System.out.println("-------------------------------");
        System.out.println("Test JSON Object parsing");
        System.out.println("-------------------------------");

        // JSON object example
        // We start with a String in JSON notation. It describes an object
        // We will try to extract the data from the JSON string
        System.out.println("Starting json string: " + jsonObjectString);

        // Let's try to parse it as a JSON object
        try {
            JSONObject jsonObject = new JSONObject(jsonObjectString);
            if (jsonObject.has("b")) {
                int b = jsonObject.getInt("b");
                System.out.println("The object contains field 'b' with value "
                        + b);
            }

            // We can also change some fields in the JSONObject
            jsonObject.put("c", 4);

            // And if we want to translate a JSON object to a string, we simply
            // use toString() method
            System.out.println("The updated JSON object as a string: "
                    + jsonObject.toString());

            // Now let's try to parse the JSON string as an array.
            // This should raise an exception, because the String does not
            // contain a JSON array
            JSONArray wrongArray = new JSONArray(jsonObjectString);

        } catch (JSONException e) {
            // It is important to always wrap JSON parsing in try/catch
            // If the string is suddently not in the expected format, 
            // an exception will be generated
            System.out.println("Got exception in JSON parsing: " + e.getMessage());
        }
        System.out.println("");
    }
}