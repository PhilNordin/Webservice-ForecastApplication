package se.systementor.dag1.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.systementor.dag1.URLS.Urls;
import se.systementor.dag1.models.Forecast;
import se.systementor.dag1.repositories.ForecastRepository;
import se.systementor.dag1.services.smhi.data.Parameter;
import se.systementor.dag1.services.smhi.data.TimeSeries;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class ForecastService {
    @Autowired
    private ForecastRepository forecastRepository;
    //private static List<Forecast> forecasts = new ArrayList<>();

    public ForecastService(){


    }

    public void fetchAndSaveToDB() throws IOException {
        var objectMapper = new ObjectMapper();

        Root root = objectMapper.readValue(new URL(Urls.smhiAPI()),
                Root.class);
        List<TimeSeries> timeseriesList = root.getTimeseries();

        Date currentTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.add(Calendar.HOUR_OF_DAY, 25);
        Date tomorrow = calendar.getTime();
        for (TimeSeries timeSeries : timeseriesList) {
            Date validTime = timeSeries.getValidTime();
            calendar.setTime(validTime);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

            LocalDate validLocalDate = validTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if(validTime.after(currentTime) && validTime.before(tomorrow) &&
                    hour == currentHour) {
                for(Parameter param : timeSeries.getParameters()) {
                    String paraName = param.getName();
                    var forecastFromSmhi = new ForeCast();
                    List<Float> values = param.getValues();

                    Boolean rainOrSnow = false;

                    for(Float paramValue :values) {
                        if("t".equals(paraName) || "pcat".equals(paraName))
                        {
                            if(paramValue == 3.0 && paramValue == 1) {
                                rainOrSnow = true;
                            }
                        }

                        if ("t".equals(paraName)) {

                            System.out.println("tid: " + hour);
                            System.out.println("temp: " + paramValue);
                            System.out.println("tid: " + validLocalDate);

                            forecastFromSmhi.setId(UUID.randomUUID());
                            forecastFromSmhi.setRainOrSnow(rainOrSnow);
                            forecastFromSmhi.setPredictionTemperature(paramValue);
                            forecastFromSmhi.setPredictionDatum(validLocalDate);
                            forecastFromSmhi.setPredictionHour(hour);
                            forecastFromSmhi.setApiProvider(DataSource.Smhi);
                            forecastRepository.save(forecastFromSmhi);

                        }
                    }
                }
            }
        }
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




