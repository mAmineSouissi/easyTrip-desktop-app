package tn.esprit.easytripdesktopapp.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CurrencyConverter {

    private static final String API_KEY = "5e02a89bff543aef53d77ca6bdedf7f5"; // Votre clé API
    private static final String BASE_URL = "http://api.exchangeratesapi.io/v1/latest?access_key=" + API_KEY + "&base=EUR&symbols=USD,GBP,JPY,CAD";

    // Méthode pour obtenir les taux de change
    public Map<String, Double> getExchangeRates() {
        Map<String, Double> rates = new HashMap<>();
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Vérifier la réponse HTTP
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 200
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Extraire les taux de change de la réponse JSON
                String jsonResponse = response.toString();
                if (jsonResponse.contains("\"rates\":")) {
                    String ratesString = jsonResponse.split("\"rates\":\\{")[1].split("\\}")[0];
                    String[] ratePairs = ratesString.split(",");

                    for (String pair : ratePairs) {
                        String[] keyValue = pair.split(":");
                        String currency = keyValue[0].replace("\"", "").trim();
                        double rate = Double.parseDouble(keyValue[1]);
                        rates.put(currency, rate);
                    }
                } else {
                    System.err.println("Erreur : Réponse JSON invalide. Réponse complète : " + jsonResponse);
                }
            } else {
                System.err.println("Erreur HTTP : " + responseCode);
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String errorLine;
                StringBuilder errorResponse = new StringBuilder();
                while ((errorLine = errorReader.readLine()) != null) {
                    errorResponse.append(errorLine);
                }
                errorReader.close();
                System.err.println("Réponse d'erreur : " + errorResponse.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rates;
    }

    // Méthode pour convertir un montant d'une devise à une autre
    public double convert(double amount, String fromCurrency, String toCurrency) {
        Map<String, Double> rates = getExchangeRates();
        double fromRate = rates.getOrDefault(fromCurrency, 1.0); // Par défaut, EUR
        double toRate = rates.getOrDefault(toCurrency, 1.0);

        // Convertir le montant
        return (amount / fromRate) * toRate;
    }
}