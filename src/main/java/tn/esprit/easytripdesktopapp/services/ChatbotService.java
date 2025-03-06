package tn.esprit.easytripdesktopapp.services;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ChatbotService {

    private static final String API_KEY = "AIzaSyDwTXOO_AUAlC6kBmHiKGpGlstlV4XV6kg";  // Remplacez par votre clé API Gemini
    private static String MODEL_NAME = "models/gemini-1.5-flash";  //Manually set model name
    // private static String MODEL_NAME = null; // Will be populated by listAvailableModels

    public static String listAvailableModels() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1/models?key=" + API_KEY)
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonResponse = new JSONObject(responseBody);
                JSONArray models = jsonResponse.getJSONArray("models");

                //Override model only if it supports generateContent and model name matches
                for (int i = 0; i < models.length(); i++) {
                    JSONObject model = models.getJSONObject(i);
                    if (model.has("name") && model.getString("name").equals("models/gemini-1.0-pro-vision-latest")) {
                        System.out.println("Skipping deprecated model: models/gemini-1.0-pro-vision-latest");
                        continue; // Skip the deprecated model
                    }
                    JSONArray supportedMethods = model.getJSONArray("supportedGenerationMethods");
                    if (supportedMethods != null) {
                        for (int j = 0; j < supportedMethods.length(); j++) {
                            if (supportedMethods.getString(j).equals("generateContent") && model.has("name") && model.getString("name").equals("models/gemini-1.5-flash") ) {
                                MODEL_NAME = model.getString("name");
                                System.out.println("Found a suitable model: " + MODEL_NAME);
                                return null; // Successfully found a model, no need to return the whole string.
                            }
                        }
                    }
                }
                return "No suitable model found with generateContent support that is not deprecated.";

            } else {
                return "Error listing models: " + response.code() + " - " + response.message();
            }
        } catch (IOException e) {
            return "Error listing models: " + e.getMessage();
        }
    }


    public static String getChatbotResponse(String userMessage) {
        //Removed MODEL_NAME == null so we skip this and always use the manual model.
        String errorMessage = listAvailableModels(); // Call listAvailableModels always, in case of errors.
        if (errorMessage != null) {
            return "Error initializing chatbot: " + errorMessage;
        }


        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        JSONObject jsonRequest = new JSONObject();
        JSONArray contentsArray = new JSONArray();
        JSONObject partsObject = new JSONObject();
        partsObject.put("text", userMessage);
        JSONArray partsArray = new JSONArray();
        partsArray.put(partsObject);

        JSONObject contentObject = new JSONObject();
        contentObject.put("parts", partsArray);

        contentsArray.put(contentObject);
        jsonRequest.put("contents", contentsArray);

        RequestBody body = RequestBody.create(jsonRequest.toString(), MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1/" + MODEL_NAME + ":generateContent?key=" + API_KEY)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                return parseResponse(responseBody);
            } else {
                System.out.println("API Error: " + response.code() + " - " + response.message());
                System.out.println("Error Response: " + response.body().string());
                return "Sorry, I couldn't understand your question. Error: " + response.code();
            }
        } catch (IOException e) {
            System.out.println("API Call Error: " + e.getMessage());
            return "An error occurred while communicating with the chatbot.";
        }
    }

    private static String parseResponse(String responseBody) {
        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            if (jsonResponse.has("candidates")) {
                return jsonResponse.getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text");
            }
            return "No response obtained from the chatbot.";
        } catch (Exception e) {
            System.err.println("Error parsing response: " + e.getMessage());
            return "Error parsing chatbot response.";
        }
    }


    /* REMOVED MAIN METHOD */
    /* REMOVED MAIN METHOD */
    /* REMOVED MAIN METHOD */
    /* REMOVED MAIN METHOD */
    /* REMOVED MAIN METHOD */
    /* REMOVED MAIN METHOD */
    /* REMOVED MAIN METHOD */


}