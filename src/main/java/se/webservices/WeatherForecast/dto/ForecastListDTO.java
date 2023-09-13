package se.webservices.WeatherForecast.dto;

import java.time.LocalDate;
import java.util.UUID;

public class ForecastListDTO {
    public UUID Id;
    public LocalDate Date;  //YYYYMMDD
    public int Hour;
    public float Temperature;

}
