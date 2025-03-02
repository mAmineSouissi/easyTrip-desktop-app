package tn.esprit.easytripdesktopapp.utils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GoogleOAuthConfig {
    private static final String APPLICATION_NAME = "EasyTrip Desktop App";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/userinfo.email",
            "https://www.googleapis.com/auth/userinfo.profile",
            "openid");
    private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";

    /**
     * Creates an authorized Credential object.
     */
    public static Credential getCredentials(final NetHttpTransport httpTransport) throws IOException {
        // Load client secrets from your credentials.json
        InputStream in = GoogleOAuthConfig.class.getResourceAsStream("/tn/esprit/easytripdesktopapp/Config/client_secret_93925146747-0ujl03p2m43ttie5n4b0rpakhrg45sjj.apps.googleusercontent.com.json");
        if (in == null) {
            throw new FileNotFoundException("Resource not found: credentials.json");
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(in, StandardCharsets.UTF_8));

        // Build flow and trigger user authorization request
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8080).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * Get user info from Google
     */
    public static Map<String, Object> getUserInfo() throws IOException, GeneralSecurityException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = getCredentials(httpTransport);

        HttpRequestFactory requestFactory = httpTransport.createRequestFactory(
                request -> {
                    request.setParser(new JsonObjectParser(JSON_FACTORY));
                    credential.initialize(request);
                });

        HttpRequest request = requestFactory.buildGetRequest(new GenericUrl(USER_INFO_URL));
        HttpResponse response = request.execute();

        // Parse the response into a Map
        @SuppressWarnings("unchecked")
        Map<String, Object> userInfo = (Map<String, Object>) JSON_FACTORY.createJsonParser(response.getContent()).parse(Map.class);

        return userInfo;
    }
}