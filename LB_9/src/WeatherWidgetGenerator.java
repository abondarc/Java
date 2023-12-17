/*import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


class WeatherData {
        @SerializedName("name")
        private String city;
        private String country;
        private Weather[] weather;
        private main main;
        private Wind wind;
        private Clouds clouds;

        // Getters and setters

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public Weather[] getWeather() {
            return weather;
        }

        public void setWeather(Weather[] weather) {
            this.weather = weather;
        }

        public main getMain() {
            return main;
        }

        public void setMain(main main) {
            this.main = main;
        }

        public Wind getWind() {
            return wind;
        }

        public void setWind(Wind wind) {
            this.wind = wind;
        }

        public Clouds getClouds() {
            return clouds;
        }

        public void setClouds(Clouds clouds) {
            this.clouds = clouds;
        }
    }

    class Weather {
        private String main;
        private String description;
        private String icon;

        // Getters and setters

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }

    class main {
        private float temp;
        private int pressure;
        private int humidity;
        @SerializedName("temp_min")
        private float minTemp;
        @SerializedName("temp_max")
        private float maxTemp;

        // Getters and setters

        public float getTemp() {
            return temp;
        }

        public void setTemp(float temp) {
            this.temp = temp;
        }

        public int getPressure() {
            return pressure;
        }

        public void setPressure(int pressure) {
            this.pressure = pressure;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public float getMinTemp() {
            return minTemp;
        }

        public void setMinTemp(float minTemp) {
            this.minTemp = minTemp;
        }

        public float getMaxTemp() {
            return maxTemp;
        }

        public void setMaxTemp(float maxTemp) {
            this.maxTemp = maxTemp;
        }
    }

    class Wind {
        private float speed;
        private int deg;

        // Getters and setters

        public float getSpeed() {
            return speed;
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }

        public int getDeg() {
            return deg;
        }

        public void setDeg(int deg) {
            this.deg = deg;
        }
    }

    class Clouds {
        private int all;

        // Getters and setters

        public int getAll() {
            return all;
        }

        public void setAll(int all) {
            this.all = all;
        }
    }

    public class WeatherWidgetGenerator {

        private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&mode=json&appid=bd82977b86bf27fb59a04b61b657fb6f";
        private static final String ICON_URL = "http://openweathermap.org/img/w/%s.png";

        public static void main(String[] args) {
            String cityName = "Homyel',by";
            WeatherData weatherData = getWeatherData(cityName);

            if (weatherData != null) {
                String html = generateWeatherWidgetHTML(weatherData);
                saveHTMLToFile(html, "weather_widget.html");
                System.out.println("Weather widget HTML saved to weather_widget.html");
            } else {
                System.out.println("Failed to fetch weather data for " + cityName);
            }
        }

        private static WeatherData getWeatherData(String cityName) {
            try {
                String apiUrl = String.format(API_URL, cityName.replace(" ", "%20"));
                URL url = new URL(apiUrl);
                InputStream inputStream = url.openStream();
                Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name()).useDelimiter("\\A");
                String json = scanner.hasNext() ? scanner.next() : "";
                scanner.close();
                Gson gson = new Gson();
                return gson.fromJson(json, WeatherData.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private static String generateWeatherWidgetHTML(WeatherData weatherData) {
            Weather weather = weatherData.getWeather()[0];
            String iconUrl = String.format(ICON_URL, weather.getIcon());

            StringBuilder htmlBuilder = new StringBuilder();
            htmlBuilder.append("<html><head><title>Weather Widget</title></head><body>");
            htmlBuilder.append("<h1>").append(weatherData.getCity()).append(", ").append(weatherData.getCountry()).append("</h1>");
            htmlBuilder.append("<img src=\"").append(iconUrl).append("\" alt=\"Weather Icon\">");
            htmlBuilder.append("<p><strong>Current Weather: </strong>").append(weather.getDescription()).append("</p>");
            htmlBuilder.append("<p><strong>Temperature: </strong>").append(weatherData.getMain().getTemp()).append("°C</p>");
            htmlBuilder.append("<p><strong>Pressure: </strong>").append(weatherData.getMain().getPressure()).append(" mmHg</p>");
            htmlBuilder.append("<p><strong>Humidity: </strong>").append(weatherData.getMain().getHumidity()).append("%</p>");
            htmlBuilder.append("<p><strong>Min Temperature: </strong>").append(weatherData.getMain().getMinTemp()).append("°C</p>");
            htmlBuilder.append("<p><strong>Max Temperature: </strong>").append(weatherData.getMain().getMaxTemp()).append("°C</p>");
            htmlBuilder.append("<p><strong>Wind: </strong>").append(weatherData.getWind().getSpeed()).append(" m/s ").append(getWindDirection(weatherData.getWind().getDeg())).append("</p>");
            htmlBuilder.append("<p><strong>Cloud Cover: </strong>").append(weatherData.getClouds().getAll()).append("%</p>");
            htmlBuilder.append("</body></html>");

            return htmlBuilder.toString();
        }

        private static String getWindDirection(int degree) {
            if (degree >= 337.5 || degree < 22.5) {
                return "N";
            } else if (degree >= 22.5 && degree < 67.5) {
                return "NE";
            } else if (degree >= 67.5 && degree < 112.5) {
                return "E";
            } else if (degree >= 112.5 && degree < 157.5) {
                return "SE";
            } else if (degree >= 157.5 && degree < 202.5) {
                return "S";
            } else if (degree >= 202.5 && degree < 247.5) {
                return "SW";
            } else if (degree >= 247.5 && degree < 292.5) {
                return "W";
            } else if (degree >= 292.5 && degree < 337.5) {
                return "NW";
            } else {
                return "";
            }
        }

        private static void saveHTMLToFile(String html, String fileName) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                writer.write(html);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
*/
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

class WeatherData {
    private String name;
    private String cod;
    private String country;
    private Weather[] weather;
    private main main;
    private Wind wind;
    private Clouds clouds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    public main getMain() {
        return main;
    }

    public void setMain(main main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }
}

class Weather {
    private String main;
    private String description;
    private String icon;

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}

class main {
    private float temp;
    private int pressure;
    private int humidity;
    @SerializedName("temp_min")
    private float minTemp;
    @SerializedName("temp_max")
    private float maxTemp;

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public float getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(float minTemp) {
        this.minTemp = minTemp;
    }

    public float getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(float maxTemp) {
        this.maxTemp = maxTemp;
    }
}

class Wind {
    private float speed;
    private int deg;

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }
}

class Clouds {
    private int all;

    public int getAll() {
        return all;
    }

    public void setAll(int all) {
        this.all = all;
    }
}

public class WeatherWidgetGenerator {

    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&mode=json&appid=bd82977b86bf27fb59a04b61b657fb6f";
    private static final String ICON_URL = "http://openweathermap.org/img/w/%s.png";

    public static void main(String[] args) {
        String cityName = "Homyel',by";
        WeatherData weatherData = getWeatherData(cityName);

        if (weatherData != null) {
            String html = generateWeatherWidgetHTML(weatherData);
            saveHTMLToFile(html, "weather_widget.html");
            System.out.println("Weather widget HTML saved to weather_widget.html");
        } else {
            System.out.println("Failed to fetch weather data for " + cityName);
        }
    }

    private static WeatherData getWeatherData(String cityName) {
        try {
            String apiUrl = String.format(API_URL, cityName.replace(" ", "%20"));
            URL url = new URL(apiUrl);
            InputStream inputStream = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(reader, WeatherData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String generateWeatherWidgetHTML(WeatherData weatherData) {
        Weather weather = weatherData.getWeather()[0];
        String iconUrl = String.format(ICON_URL, weather.getIcon());

        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html><head><title>Weather Widget</title></head><body>");
        htmlBuilder.append("<h2>").append(weatherData.getName()).append(", ").append(weatherData.getCountry()).append("</h2>");
        htmlBuilder.append("<img src=\"").append(iconUrl).append("\">");
        htmlBuilder.append("<p><b>Current weather:</b> ").append(weather.getMain()).append("</p>");
        htmlBuilder.append("<p><b>Description:</b> ").append(weather.getDescription()).append("</p>");
        htmlBuilder.append("<p><b>Temperature:</b> ").append(weatherData.getMain().getTemp()).append(" °C</p>");
        htmlBuilder.append("<p><b>Pressure:</b> ").append(weatherData.getMain().getPressure()).append(" mmHg</p>");
        htmlBuilder.append("<p><b>Humidity:</b> ").append(weatherData.getMain().getHumidity()).append("%</p>");
        htmlBuilder.append("<p><b>Min/Max Temperature:</b> ").append(weatherData.getMain().getMinTemp()).append(" °C / ")
                .append(weatherData.getMain().getMaxTemp()).append(" °C</p>");
        htmlBuilder.append("<p><b>Wind Speed/Direction:</b> ").append(weatherData.getWind().getSpeed()).append(" m/s ")
                .append(getWindDirection(weatherData.getWind().getDeg())).append("</p>");
        htmlBuilder.append("<p><b>Cloud Cover:</b> ").append(weatherData.getClouds().getAll()).append("%</p>");
        htmlBuilder.append("</body></html>");

        return htmlBuilder.toString();
    }

    private static String getWindDirection(int degrees) {
        if (degrees >= 337.5 || degrees < 22.5) {
            return "N";
        } else if (degrees >= 22.5 && degrees < 67.5) {
            return "NE";
        } else if (degrees >= 67.5 && degrees < 112.5) {
            return "E";
        } else if (degrees >= 112.5 && degrees < 157.5) {
            return "SE";
        } else if (degrees >= 157.5 && degrees < 202.5) {
            return "S";
        } else if (degrees >= 202.5 && degrees < 247.5) {
            return "SW";
        } else if (degrees >= 247.5 && degrees < 292.5) {
            return "W";
        } else {
            return "NW";
        }
    }

    private static void saveHTMLToFile(String html, String fileName) {
        try (PrintWriter writer = new PrintWriter(fileName)) {
            writer.println(html);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}