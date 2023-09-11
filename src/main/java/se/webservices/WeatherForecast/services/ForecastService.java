package se.webservices.WeatherForecast.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.webservices.WeatherForecast.URLS.Urls;
import se.webservices.WeatherForecast.models.Forecast;
import se.webservices.WeatherForecast.repositories.ForecastRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ForecastService {
    private final ForecastRepository forecastRepository;
    @Autowired
    public ForecastService(ForecastRepository forecastRepository){
        this.forecastRepository = forecastRepository;
    }

    private static List<Forecast> forecasts = new ArrayList<>();

    public List<Forecast> getForecast(LocalDate now) {
        List<Forecast> forecasts = forecastRepository.findAll();

        return forecastRepository.findAll();
    }

    public Forecast add(Forecast forecast) throws IOException {
        forecastRepository.save(forecast);
        return forecast;
    }

    public void update (Forecast forecastFromUser) throws IOException {
        forecastRepository.save(forecastFromUser);
    }

    public void delete(UUID id) {
        forecastRepository.deleteById(id);
        System.out.println("\n*----- DELETED -----*");
    }

    public Optional <Forecast> get (UUID id) {
        return forecastRepository.findById(id);
    }

}

