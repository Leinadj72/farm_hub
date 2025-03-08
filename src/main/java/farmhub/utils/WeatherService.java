package farmhub.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;
import farmhub.models.WeatherModel;

public class WeatherService {
    private static final String API_KEY = "65c5605c4336415d8ad94208252602";
    private static final String BASE_URL = "http://api.weatherapi.com/v1/current.json?key=";

    public static WeatherModel getWeather(String location) throws IOException {
        String urlString = BASE_URL + API_KEY + "&q=" + location;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        Scanner scanner = new Scanner(url.openStream());
        StringBuilder result = new StringBuilder();
        while (scanner.hasNext()) {
            result.append(scanner.nextLine());
        }
        scanner.close();

        return parseWeatherData(result.toString());
    }

    private static WeatherModel parseWeatherData(String jsonResponse) {
        JSONObject obj = new JSONObject(jsonResponse);
        JSONObject current = obj.getJSONObject("current");
        JSONObject condition = current.getJSONObject("condition");

        return new WeatherModel(
                obj.getJSONObject("location").getString("name"),
                current.getDouble("temp_c"),
                current.getInt("humidity"),
                condition.getString("text")
        );
    }
}
