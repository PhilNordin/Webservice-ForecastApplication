package se.webservices.WeatherForecast.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.webservices.WeatherForecast.URLS.Urls;
import se.webservices.WeatherForecast.dto.AverageDTO;
import se.webservices.WeatherForecast.models.Forecast;
import se.webservices.WeatherForecast.repositories.ForecastRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class ForecastService {

    @Autowired
    private ForecastRepository forecastRepository;
//    @Autowired
//    public ForecastService(ForecastRepository forecastRepository){
//        this.forecastRepository = forecastRepository;
//    }

//    private static List<Forecast> forecasts = new ArrayList<>();

    public List<Forecast> getForecasts() {
        return forecastRepository.findAll();
    }

    public Forecast add(Forecast forecast) throws IOException {
        forecastRepository.save(forecast);
        return forecast;
    }

    public Forecast getByIndex(int i){return getForecasts().get(i);}

    public void update (Forecast forecastFromUser) throws IOException {
        forecastRepository.save(forecastFromUser);
    }

    public void deleteById(UUID id) {
        forecastRepository.deleteById(id);
        System.out.println("\n*----- DELETED -----*");
    }

    public Optional <Forecast> get (UUID id) {
        return forecastRepository.findById(id);
    }

    public void deleted(Forecast forecast) {
        forecastRepository.deleteById(forecast.getId());
    }

    public List<AverageDTO> calculateAverage(LocalDate dag) {
        var resultList = new ArrayList<AverageDTO>();

        // Hämta alla poster från databasen för det angivna datumet
        var allForecastsForDay = forecastRepository.findAllByDate(dag);

        // Hämta aktuell timme
        LocalTime currentTime = LocalTime.now();
        int currentHour = currentTime.getHour();


//        Date currentTime = new Date();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(currentTime);
//        calendar.add(Calendar.HOUR_OF_DAY, 24);
//        Date tomorrow = calendar.getTime();
//
//        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);


        // Loopa genom timmarna för de närmaste 24 timmarna
        for (int hour = currentHour; hour < currentHour + 24; hour++) {
            var averageDto = new AverageDTO();
            averageDto.setHour(hour % 24); // Hantera passering av midnatt (00:00)
            averageDto.setDate(dag);
            float antal = 0;
            float sum = 0;

            // Loopa genom alla prognoser för dagen och beräkna genomsnittet för den aktuella timmen
            for (Forecast forecast : allForecastsForDay) {
                if (forecast.getHour() == hour % 24) {
                    antal++;
                    sum += forecast.getTemperature();
                }
            }

            if (antal > 0) {
                averageDto.setAverage(sum / antal);
                resultList.add(averageDto);
            }
        }

        return resultList; // size = 24
    }
}

