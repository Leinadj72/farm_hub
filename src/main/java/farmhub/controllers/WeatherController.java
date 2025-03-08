// Dashboard Controller
package farmhub.controllers;

import farmhub.dao.WeatherDAO;
import farmhub.models.WeatherModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WeatherController {
    @FXML
    private Label locationLabel;
    @FXML
    private Label temperatureLabel;
    @FXML
    private Label humidityLabel;
    @FXML
    private Label conditionLabel;

    public void initialize() {
        loadWeather("London"); // Default location
    }

    private void loadWeather(String location) {
        WeatherModel weather = WeatherDAO.fetchWeather(location);
        if (weather != null) {
            locationLabel.setText(weather.getLocation());
            temperatureLabel.setText(weather.getTemperature() + " °C");
            humidityLabel.setText(weather.getHumidity() + "%");
            conditionLabel.setText(weather.getCondition());
        } else {
            locationLabel.setText("Error fetching weather");
        }
    }
}
