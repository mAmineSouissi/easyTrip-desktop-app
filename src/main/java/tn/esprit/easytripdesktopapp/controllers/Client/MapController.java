package tn.esprit.easytripdesktopapp.controllers.Client;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
//import com.fasterxml.jackson.*;databind.JsonNode;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapController {

    @FXML
    private WebView mapView;

    private String location; // Store the car's location
    private String apiKey = "84342dee726ddb8449428bd19d8f5e54c2c29f42f3c2a6503e8e95df9cd1b57e"; // Replace with your SerpApi key

    // Method to set the car's location
    public void setLocation(String location) {
        this.location = location;
        if (location != null) {
            loadMap(); // Load the map with the coordinates
        } else {
            System.out.println("Could not find coordinates for the location.");
        }
    }



    // Method to fetch and extract raw HTML link
    private void loadMap() {
        OkHttpClient client = new OkHttpClient();

        // Build the API URL
        String apiUrl = "https://serpapi.com/search.json?engine=google_maps&q=" + location + "&api_key=" + apiKey;
        System.out.println("API URL: " + apiUrl);

        // Create the request
        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonResponse = response.body().string();
               // System.out.println("JSON Response: " + jsonResponse);

                // Extract the raw_html_file link using Jackson-Core
                String rawHtmlFileLink = extractRawHtmlFile(jsonResponse);

                if (rawHtmlFileLink != null) {
                    System.out.println("Raw HTML File Link: " + rawHtmlFileLink);
                    mapView.getEngine().load(rawHtmlFileLink);
                    return;
                }
            }
        } catch (IOException e) {
            System.err.println("Error fetching raw HTML file link: " + e.getMessage());
        }

        // If the raw HTML file link is not found, load a fallback URL
        System.out.println("Failed to fetch raw HTML file link.");
        mapView.getEngine().load("https://www.google.com/maps"); // Fallback URL
    }

    // Extract "raw_html_file" from JSON manually using Jackson-Core
    private String extractRawHtmlFile(String json) throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(json);

        while (!parser.isClosed()) {  // Corrected while condition
            JsonToken token = parser.nextToken();

            // Check if the current field name is "raw_html_file"
            if (token == JsonToken.FIELD_NAME && "raw_html_file".equals(parser.getCurrentName())) {
                parser.nextToken(); // Move to the value of "raw_html_file"
                return parser.getValueAsString(); // Extract and return the value
            }
        }
        return null; // Return null if "raw_html_file" was not found
    }




}