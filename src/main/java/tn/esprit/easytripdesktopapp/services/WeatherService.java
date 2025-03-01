package tn.esprit.easytripdesktopapp.services;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class WeatherService {

    private static final String API_KEY = "13e6870ed026830e0391902614b56e2b"; // Remplacez par votre clé API OpenWeatherMap
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";

    /**
     * Récupère les informations météo pour une ville donnée.
     *
     * @param city Le nom de la ville.
     * @return Une chaîne de caractères contenant les informations météo.
     */
    public String getWeatherByCity(String city) {
        String url = BASE_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric"; // units=metric pour Celsius

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);

            if (response.getStatusLine().getStatusCode() == 200) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                JSONObject jsonObject = new JSONObject(jsonResponse);

                // Extraire les informations météo
                JSONObject main = jsonObject.getJSONObject("main");
                double temp = main.getDouble("temp");
                double humidity = main.getDouble("humidity");
                JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
                String description = weather.getString("description");

                return String.format("Température: %.1f°C, Humidité: %.1f%%, Description: %s", temp, humidity, description);
            } else {
                return "Erreur lors de la récupération des données météo.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la connexion à l'API météo.";
        }
    }
}