package com.weather;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class WeatherApp extends Application {
    
    private WeatherService weatherService;
    
    private TextField cityInput;
    private Label cityNameLabel;
    private Label temperatureLabel;
    private Label descriptionLabel;
    private Label feelsLikeLabel;
    private Label humidityLabel;
    private Label windSpeedLabel;
    private Label pressureLabel;
    private ImageView weatherIcon;
    private VBox weatherInfoBox;
    private Label errorLabel;

    @Override
    public void start(Stage primaryStage) {
        weatherService = new WeatherService();
        
        primaryStage.setTitle("Weather Forecast App");
        
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #2193b0, #6dd5ed);");
        
        VBox topSection = createTopSection();
        root.setTop(topSection);
        
        VBox centerSection = createCenterSection();
        root.setCenter(centerSection);
        
        Scene scene = new Scene(root, 500, 700);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private VBox createTopSection() {
        VBox topBox = new VBox(15);
        topBox.setPadding(new Insets(20));
        topBox.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("Weather Forecast");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: white;");
        
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER);
        
        cityInput = new TextField();
        cityInput.setPromptText("Enter city name...");
        cityInput.setPrefWidth(300);
        cityInput.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
        
        Button searchButton = new Button("Search");
        searchButton.setStyle(
            "-fx-background-color: #4CAF50; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 10px 20px; " +
            "-fx-cursor: hand;"
        );
        
        cityInput.setOnAction(e -> searchWeather());
        searchButton.setOnAction(e -> searchWeather());
        
        searchBox.getChildren().addAll(cityInput, searchButton);
        
        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #ff6b6b; -fx-font-size: 14px;");
        errorLabel.setVisible(false);
        
        topBox.getChildren().addAll(titleLabel, searchBox, errorLabel);
        
        return topBox;
    }

    private VBox createCenterSection() {
        weatherInfoBox = new VBox(20);
        weatherInfoBox.setPadding(new Insets(30));
        weatherInfoBox.setAlignment(Pos.CENTER);
        weatherInfoBox.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9); " +
            "-fx-background-radius: 15px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);"
        );
        weatherInfoBox.setVisible(false);
        
        cityNameLabel = new Label();
        cityNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        cityNameLabel.setStyle("-fx-text-fill: #333;");
        
        weatherIcon = new ImageView();
        weatherIcon.setFitWidth(120);
        weatherIcon.setFitHeight(120);
        weatherIcon.setPreserveRatio(true);
        
        temperatureLabel = new Label();
        temperatureLabel.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        temperatureLabel.setStyle("-fx-text-fill: #ff6b6b;");
        
        descriptionLabel = new Label();
        descriptionLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        descriptionLabel.setStyle("-fx-text-fill: #666;");
        
        Separator separator = new Separator();
        separator.setPrefWidth(200);
        
        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(40);
        infoGrid.setVgap(15);
        infoGrid.setAlignment(Pos.CENTER);
        
        Label feelsLikeTitle = createInfoTitle("Feels Like:");
        feelsLikeLabel = createInfoValue();
        
        Label humidityTitle = createInfoTitle("Humidity:");
        humidityLabel = createInfoValue();
        
        Label windTitle = createInfoTitle("Wind Speed:");
        windSpeedLabel = createInfoValue();
        
        Label pressureTitle = createInfoTitle("Pressure:");
        pressureLabel = createInfoValue();
        
        infoGrid.add(feelsLikeTitle, 0, 0);
        infoGrid.add(feelsLikeLabel, 1, 0);
        infoGrid.add(humidityTitle, 0, 1);
        infoGrid.add(humidityLabel, 1, 1);
        infoGrid.add(windTitle, 0, 2);
        infoGrid.add(windSpeedLabel, 1, 2);
        infoGrid.add(pressureTitle, 0, 3);
        infoGrid.add(pressureLabel, 1, 3);
        
        weatherInfoBox.getChildren().addAll(
            cityNameLabel,
            weatherIcon,
            temperatureLabel,
            descriptionLabel,
            separator,
            infoGrid
        );
        
        return weatherInfoBox;
    }

    private Label createInfoTitle(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        label.setStyle("-fx-text-fill: #666;");
        return label;
    }

    private Label createInfoValue() {
        Label label = new Label();
        label.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        label.setStyle("-fx-text-fill: #333;");
        return label;
    }

    private void searchWeather() {
        String city = cityInput.getText().trim();
        
        if (city.isEmpty()) {
            showError("Please enter a city name");
            return;
        }
        
        try {
            errorLabel.setVisible(false);
            weatherInfoBox.setVisible(false);
            
            WeatherData weatherData = weatherService.getWeatherByCity(city);
            
            displayWeather(weatherData);
            
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void displayWeather(WeatherData data) {
        cityNameLabel.setText(data.getCityName());
        temperatureLabel.setText(String.format("%.1f°C", data.getTemperature()));
        descriptionLabel.setText(capitalizeWords(data.getDescription()));
        feelsLikeLabel.setText(String.format("%.1f°C", data.getFeelsLike()));
        humidityLabel.setText(data.getHumidity() + "%");
        windSpeedLabel.setText(String.format("%.1f m/s", data.getWindSpeed()));
        pressureLabel.setText(data.getPressure() + " hPa");
        
        String iconUrl = weatherService.getIconUrl(data.getIcon());
        Image icon = new Image(iconUrl);
        weatherIcon.setImage(icon);
        
        weatherInfoBox.setVisible(true);
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        weatherInfoBox.setVisible(false);
    }

    private String capitalizeWords(String str) {
        String[] words = str.split(" ");
        StringBuilder result = new StringBuilder();
        
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1))
                      .append(" ");
            }
        }
        
        return result.toString().trim();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
                           