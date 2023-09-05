package se.systementor.dag1.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.systementor.dag1.models.Forecast;
import se.systementor.dag1.repositories.ForecastRepository;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

@Service
public class ForecastService {
    @Autowired
    private ForecastRepository forecastRepository;
    //private static List<Forecast> forecasts = new ArrayList<>();

    public ForecastService(){

    }







    public List<Forecast> getForecasts(){
        return forecastRepository.findAll();
    }
    public Forecast add(Forecast forecast) {
        forecastRepository.save(forecast);
        return forecast;
    }

    public Forecast getByIndex(int i) {

        return null;
    }

    public void update(Forecast forecastFromUser) throws IOException {
        //
//        var foreCastInList = get(forecastFromUser.getId()).get();
//        foreCastInList.setTemperature(forecastFromUser.getTemperature());
//        foreCastInList.setDate(forecastFromUser.getDate());
//        foreCastInList.setHour(forecastFromUser.getHour());
//        foreCastInList.setLastModifiedBy(forecastFromUser.getLastModifiedBy());
//        writeAllToFile(forecasts);
    }

    public Optional<Forecast> get(UUID id) {
        return forecastRepository.findById(id);
//        return getForecasts().stream().filter(forecast -> forecast.getId().equals(id))
//                .findFirst();
    }

    public void getAllOnDate(LocalDate now) {
        //return forecastRepository.
    }
}




