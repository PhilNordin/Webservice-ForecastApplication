package se.webservices.WeatherForecast.services.smhi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.webservices.WeatherForecast.models.ForeCast;
import se.webservices.WeatherForecast.repositories.ForecastRepository;

import java.util.*;

@Service
public class ForeCastService {
    private final ForecastRepository forecastRepository;
    @Autowired
    public ForeCastService(ForecastRepository forecastRepository){
        this.forecastRepository = forecastRepository;
    }

    private static List<ForeCast> forecasts = new ArrayList<>();

    public List<ForeCast> getForecast(UUID id) {
        List<ForeCast> forecasts = forecastRepository.findAll();
        
        for (ForeCast forecast : forecasts){
            System.out.println("Forecast ID:" + forecast.getId());
            System.out.println("Longitude: " + forecast.getLongitude());
            System.out.println("Latitude: " + forecast.getLatitude());
            System.out.println("Prediction Datum: " + forecast.getPredictionDatum());
            System.out.println("-------------");
        }
        return forecasts;
    }
}
