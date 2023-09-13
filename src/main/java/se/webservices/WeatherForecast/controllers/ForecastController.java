package se.webservices.WeatherForecast.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.webservices.WeatherForecast.dto.ForecastListDTO;
import se.webservices.WeatherForecast.dto.NewForecastDTO;
import se.webservices.WeatherForecast.models.Forecast;
import se.webservices.WeatherForecast.services.ForecastService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class ForecastController {
    @Autowired
    private ForecastService forecastService;
    @GetMapping("/api/forecasts")
    public ResponseEntity<List<ForecastListDTO>> getAll(){
        return new ResponseEntity<List<ForecastListDTO>>(forecastService.getForecasts().stream().map(forecast->{
            var forecastListDTO = new ForecastListDTO();
            forecastListDTO.Id = forecast.getId();
            forecastListDTO.Date = forecast.getDate();
            forecastListDTO.Temperature = forecast.getTemperature();
            forecastListDTO.Hour = forecast.getHour();
            return forecastListDTO;
        }).collect(Collectors.toList()), HttpStatus.OK);


    }

    @DeleteMapping ("/api/forecasts/{id}")
    public ResponseEntity<Forecast> Get(@PathVariable UUID id){
        Optional<Forecast> forecast = forecastService.get(id);
        if(forecast.isPresent()) return ResponseEntity.ok(forecast.get());
        return  ResponseEntity.notFound().build();
    }

    @GetMapping("/api/forecasts/{id}")
    public ResponseEntity<Forecast> get(@PathVariable UUID id){
        Optional<Forecast> forecast = forecastService.get(id);
        if(forecast.isPresent()) return ResponseEntity.ok(forecast.get());
        return  ResponseEntity.notFound().build();
    }

    @PutMapping("/api/forecasts/{id}")
    public ResponseEntity<Forecast> update(@PathVariable UUID id, @RequestBody NewForecastDTO newForecastDTO) throws IOException {
        // mappa från dto -> entitet
        var forecast = forecastService.get(id).get();
        forecast.setId(id);
        forecast.setDate(newForecastDTO.getDate());
        forecast.setHour(newForecastDTO.getHour());
        forecast.setTemperature(newForecastDTO.getTemperature());
        forecastService.update(forecast);
        return ResponseEntity.ok(forecast);
    }

    @PostMapping("/api/forecasts")
    public ResponseEntity<Forecast> newForecast( @RequestBody Forecast forecast) throws IOException { // id
        forecastService.add(forecast);
        return ResponseEntity.ok(forecast); // mer REST ful = created (204) samt url till produkten
    }



//    @PutMapping("/api/products/{id}")
//    public ResponseEntity<Product> Update(@PathVariable UUID id, @RequestBody Product product){
//        boolean status = productService.update(product);
//        if(status == true)
//            return ResponseEntity.ok(product);
//        else
//            return ResponseEntity.badRequest().build();
//    }


}