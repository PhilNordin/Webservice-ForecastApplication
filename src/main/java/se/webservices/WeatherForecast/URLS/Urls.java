package se.webservices.WeatherForecast.URLS;

import org.springframework.beans.factory.annotation.Autowired;

public class Urls {
    public static String smhiAPI(){
        return "https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/18.0215/lat/59.3099/data.json";
    }
}
