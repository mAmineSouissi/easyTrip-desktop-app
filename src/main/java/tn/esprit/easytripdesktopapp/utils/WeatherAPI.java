package tn.esprit.easytripdesktopapp.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class WeatherAPI {

    private static final String API_KEY = "13e6870ed026830e0391902614b56e2b"; // Remplacez par votre clé API OpenWeatherMap
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";

    public static String getWeather(String city) {
        String url = BASE_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric"; // units=metric pour avoir la température en Celsius

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);

            String jsonResponse = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = new JSONObject(jsonResponse);


            JSONObject main = jsonObject.getJSONObject("main");
            double temp = main.getDouble("temp");
            int humidity = main.getInt("humidity");
            JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
            String description = weather.getString("description");

            return String.format("Température: %.1f°C, Humidité: %d%%, Conditions: %s", temp, humidity, description);
        } catch (Exception e) {
            e.printStackTrace();
            return "Impossible de récupérer les données météo.";
        }
    }
}