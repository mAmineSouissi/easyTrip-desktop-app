package tn.esprit.easytripdesktopapp.controllers;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ChatbotController {

    // Load the API key from config.properties file
    private static String API_KEY;

    @FXML private TextArea chatArea;
    @FXML private TextField userInput;

    private OkHttpClient client;

    // Static block to load the API_KEY when the class is loaded
    static {
        Properties properties = new Properties();
        try (InputStream input = ChatbotController.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
                API_KEY = properties.getProperty("API_KEY"); // Get API_KEY from config.properties
            } else {
                System.out.println("Sorry, unable to find config.properties");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void initialize() {
        client = new OkHttpClient();
        chatArea.appendText("Chatbot: Hello! How can I help you?\n");
    }

    @FXML
    private void handleSend() {
        String message = userInput.getText().trim();
        if (message.isEmpty()) return;

        // Display user message
        chatArea.appendText("You: " + message + "\n");
        userInput.clear();

        // Create a task to run the API request in the background
        Task<Void> chatTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    // Send message to Cohere API
                    String response = sendToCohereAPI(message);

                    // Update chat area on JavaFX Application thread
                    javafx.application.Platform.runLater(() -> chatArea.appendText("Chatbot: " + response + "\n"));
                } catch (IOException e) {
                    javafx.application.Platform.runLater(() -> chatArea.appendText("Chatbot: An error occurred. Please try again later.\n"));
                    e.printStackTrace();
                }
                return null;
            }
        };

        // Start the task in the background
        new Thread(chatTask).start();
    }

    private String sendToCohereAPI(String message) throws IOException {
        // Build JSON request body
        JSONObject json = new JSONObject();
        json.put("prompt", message);
        json.put("max_tokens", 50);

        // Create request body
        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.get("application/json")
        );

        // Create request
        Request request = new Request.Builder()
                .url("https://api.cohere.ai/v1/generate")
                .header("Authorization", "Bearer " + API_KEY) // Use API_KEY loaded from config.properties
                .post(body)
                .build();

        // Execute request and get response
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                // Parse the response
                String responseBody = response.body().string();
                JSONObject responseJson = new JSONObject(responseBody);
                return responseJson.getJSONArray("generations").getJSONObject(0).getString("text").trim();
            } else {
                return "Error: " + response.message();
            }
        }
    }
}
