
// Weather DAO
package farmhub.dao;

import farmhub.models.WeatherModel;
import farmhub.utils.WeatherService;
import java.io.IOException;

public class WeatherDAO {
    public static WeatherModel fetchWeather(String location) {
        try {
            return WeatherService.getWeather(location);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}