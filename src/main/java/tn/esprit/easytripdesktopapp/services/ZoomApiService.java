package tn.esprit.easytripdesktopapp.services;

import okhttp3.*;
import com.google.gson.Gson;
import tn.esprit.easytripdesktopapp.models.Webinaire;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ZoomApiService {
    private static final String ZOOM_API_URL = "https://api.zoom.us/v2/users/me/webinars";
    private static final String CLIENT_ID = "rQ7vz1ltToGvECMG1XBwyw";
    private static final String CLIENT_SECRET = "D3NIql47f0eOsRaf7tSKAPscEjljFwFM";
    private static final String TOKEN_URL = "https://zoom.us/oauth/token";

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    private String accessToken;
    private long tokenExpirationTime;

    public String getAccessToken() throws IOException {
        if (accessToken == null || System.currentTimeMillis() > tokenExpirationTime) {
            // Encoder le Client ID et le Client Secret en Base64
            String credentials = Credentials.basic(CLIENT_ID, CLIENT_SECRET);

            // Corps de la requête pour obtenir un token d'accès
            RequestBody body = new FormBody.Builder()
                    .add("grant_type", "client_credentials")
                    .build();

            // Créer la requête HTTP
            Request request = new Request.Builder()
                    .url(TOKEN_URL)
                    .post(body)
                    .addHeader("Authorization", credentials)
                    .build();

            // Exécuter la requête
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Erreur lors de la récupération du token OAuth : " + response.body().string());
                }
                // Extraire le token d'accès de la réponse JSON
                String jsonResponse = response.body().string();
                this.accessToken = gson.fromJson(jsonResponse, AccessTokenResponse.class).getAccessToken();
                this.tokenExpirationTime = System.currentTimeMillis() + 3600 * 1000; // 1 heure
            }
        }
        return accessToken;
    }

    public String createWebinar(Webinaire webinaire) throws IOException {
        String accessToken = getAccessToken();

        // Convertir les dates en format ISO 8601
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String startTime = webinaire.getDebutDateTime().format(formatter);

        // Créer le corps de la requête JSON
        String jsonBody = String.format(
                "{\"topic\": \"%s\", \"type\": 5, \"start_time\": \"%s\", \"duration\": 60, \"timezone\": \"UTC\", \"password\": \"123456\", \"agenda\": \"%s\"}",
                webinaire.getTitle(), startTime, webinaire.getDescription()
        );

        // Créer la requête HTTP
        Request request = new Request.Builder()
                .url(ZOOM_API_URL)
                .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        // Exécuter la requête
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erreur lors de la création du webinaire : " + response.body().string());
            }
            return response.body().string();
        }
    }

    // Classe pour désérialiser la réponse JSON du token OAuth
    private static class AccessTokenResponse {
        private String access_token;

        public String getAccessToken() {
            return access_token;
        }
    }
}