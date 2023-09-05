package se.webservices.WeatherForecast.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.webservices.WeatherForecast.dto.ForecastListDTO;
import se.webservices.WeatherForecast.dto.NewForecastDTO;
import se.webservices.WeatherForecast.models.ForeCast;
import se.webservices.WeatherForecast.services.smhi.ForeCastService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/*
* SÅ man skapar ett nytt DTO baserat på vem som använder tjänsten helt enkelt?
*
*
* kan man inte via repositoryt begränsa vad som ska visas och använda det i controllern?
* */

// 1, CLienbt anriooar /api/forecasts GET
//2. Spring kollar vilken funktion hanterar denna /api/forecasts
//3. Spring anropar den funktionen
// 3.5 VÅR KOD KÖRS
//4. Spring tar det som funktionen returnerar och gör till JSON
//5. Spring skickar tillbaka JSON till client


// varför gör man det som en stream och inte bara en for loop?

@RestController
public class SmhiController {
    @Autowired
    private ForeCastService foreCastService;
    @GetMapping("/api/forecasts")
    public ResponseEntity<List<ForecastListDTO>> getAll(){

//        var ret = new ArrayList<ForecastListDTO>();
//        for(var c : forecastService.getForecasts()){
//            var forecastListDTO = new ForecastListDTO();
//            forecastListDTO.Id = c.getId();
//            forecastListDTO.Date = c.getDate();
//            forecastListDTO.Temperature = c.getTemperature();
//            forecastListDTO.Hour = c.getHour();
//            ret.add(forecastListDTO);
//        }
//        return ret;

        return new ResponseEntity<List<ForecastListDTO>>(foreCastService.getForecasts().stream().map(forecast->{
            var forecastListDTO = new ForecastListDTO();
            forecastListDTO.Id = forecast.getId();
            return forecastListDTO;
        }).collect(Collectors.toList()), HttpStatus.OK);
    }

//    @deleteGetMapping("/api/forecasts/{id}")
//    public ResponseEntity<Forecast> Get(@PathVariable UUID id){
//        Optional<Forecast> forecast = forecastService.get(id);
//        if(forecast.isPresent()) return ResponseEntity.ok(forecast.get());
//        return  ResponseEntity.notFound().build();
//    }

    @GetMapping("/api/forecasts/{id}")
    public ResponseEntity<ForeCast> get(@PathVariable UUID id){
        Optional<ForeCast> forecast = forecastService.get(id);
        if(forecast.isPresent()) return ResponseEntity.ok(forecast.get());
        return  ResponseEntity.notFound().build();
    }

    @PutMapping("/api/forecasts/{id}")
    public ResponseEntity<ForeCast> update(@PathVariable UUID id, @RequestBody NewForecastDTO newForecastDTO) throws IOException {
        // mappa från dto -> entitet
        var forecast = new ForeCast();
        forecast.setId(id);
//        forecast.setDate(newForecastDTO.getDate());
//        forecast.setHour(newForecastDTO.getHour());
//        forecast.setTemperature(newForecastDTO.getTemperature());
//        forecast.setLastModifiedBy("Stefan Holmberg");
        forecastService.update(forecast);
        return ResponseEntity.ok(forecast);
    }

    @PostMapping("/api/forecasts")
    public ResponseEntity<ForeCast> newForecast(@RequestBody ForeCast forecast) throws IOException { // id
        var newCreated = forecastService.add(forecast);
        return ResponseEntity.ok(newCreated); // mer REST ful = created (204) samt url till produkten
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
