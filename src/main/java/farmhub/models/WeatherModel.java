// Weather model
package farmhub.models;

public class WeatherModel {
    private String location;
    private double temperature;
    private int humidity;
    private String condition;

    public WeatherModel(String location, double temperature, int humidity, String condition) {
        this.location = location;
        this.temperature = temperature;
        this.humidity = humidity;
        this.condition = condition;
    }

    public String getLocation() { return location; }
    public double getTemperature() { return temperature; }
    public int getHumidity() { return humidity; }
    public String getCondition() { return condition; }
}
