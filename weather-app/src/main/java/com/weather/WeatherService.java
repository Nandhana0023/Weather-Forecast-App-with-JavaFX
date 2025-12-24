package com.weather;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherService {
    // YOUR API KEY HERE
    private static final String API_KEY = "031710e79e21c1a641c148a563df5270";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
    
    private final HttpClient httpClient;

    public WeatherService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public WeatherData getWeatherByCity(String city) throws Exception {
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City name cannot be empty");
        }

        String url = String.format("%s?q=%s&appid=%s&units=metric",
                BASE_URL, 
                city.trim().replace(" ", "%20"),
                API_KEY);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, 
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return parseWeatherData(response.body());
        } else if (response.statusCode() == 404) {
            throw new Exception("City not found. Please check the city name.");
        } else if (response.statusCode() == 401) {
            throw new Exception("Invalid API key. Please check your API key.");
        } else {
            throw new Exception("Failed to fetch weather data. Status: " + response.statusCode());
        }
    }

    private WeatherData parseWeatherData(String jsonResponse) {
        WeatherData weatherData = new WeatherData();

        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

        weatherData.setCityName(jsonObject.get("name").getAsString());

        JsonObject main = jsonObject.getAsJsonObject("main");
        weatherData.setTemperature(main.get("temp").getAsDouble());
        weatherData.setFeelsLike(main.get("feels_like").getAsDouble());
        weatherData.setHumidity(main.get("humidity").getAsInt());
        weatherData.setPressure(main.get("pressure").getAsInt());
        weatherData.setTempMin(main.get("temp_min").getAsDouble());
        weatherData.setTempMax(main.get("temp_max").getAsDouble());

        JsonArray weatherArray = jsonObject.getAsJsonArray("weather");
        if (weatherArray.size() > 0) {
            JsonObject weather = weatherArray.get(0).getAsJsonObject();
            weatherData.setDescription(weather.get("description").getAsString());
            weatherData.setIcon(weather.get("icon").getAsString());
        }

        JsonObject wind = jsonObject.getAsJsonObject("wind");
        weatherData.setWindSpeed(wind.get("speed").getAsDouble());

        return weatherData;
    }

    public String getIconUrl(String iconCode) {
        return String.format("https://openweathermap.org/img/wn/%s@2x.png", iconCode);
    }
}