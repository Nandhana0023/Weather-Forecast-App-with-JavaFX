# Weather-Forecast-App-with-JavaFX

Author: Nandhana MA

Project Overview: A JavaFX desktop application that fetches and displays real-time weather 
information for any city using the OpenWeatherMap API.

## Technologies Used

- Java 25
- JavaFX 21
- Maven 3.9.12
- Gson (JSON parsing)
- OpenWeatherMap API
- HTTP Client (Java 11+)

## Features

✅ Real-time weather data fetching

✅ Search any city worldwide

✅ Beautiful gradient UI design

✅ Weather icons display

✅ Detailed weather information:
   - Temperature
   - Feels Like temperature
   - Humidity
   - Wind Speed
   - Atmospheric Pressure

✅ Error handling for invalid cities

✅ Input validation

## Project Structue

```
weather-app/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── weather/
│                   ├── WeatherApp.java      (Main UI)
│                   ├── WeatherService.java  (API calls)
│                   └── WeatherData.java     (Data model)
└── pom.xml                                  (Maven config)

```

## How to Run

Prerequisites:
- Java JDK 11 or higher
- Maven 3.6 or higher
- Internet connection

Steps:
1. Open Terminal
2. Navigate to project folder:
   cd weather-app
3. Run the application:
   mvn clean javafx:run
4. Application window will appear
5. Enter city name and click Search

## API Information

API Provider: OpenWeatherMap

API Endpoint: https://api.openweathermap.org/data/2.5/weather

Request Type: GET

Parameters:
  - q: City name
  - appid: API key
  - units: metric (Celsius)

## Code Explanation

1. WeatherData.java
   - POJO (Plain Old Java Object)
   - Stores weather information
   - Contains getters and setters
   - Represents data model

2. WeatherService.java
   - Handles API communication
   - Makes HTTP requests
   - Parses JSON responses
   - Error handling for API failures

3. WeatherApp.java
   - Main JavaFX application
   - Creates user interface
   - Handles user interactions
   - Displays weather data
   - Event handling

## Output Image

<img width="498" height="717" alt="Screenshot 2025-12-24 at 12 58 04 PM" src="https://github.com/user-attachments/assets/f1211685-4b7a-41a2-8e3d-6c5b25630118" />

