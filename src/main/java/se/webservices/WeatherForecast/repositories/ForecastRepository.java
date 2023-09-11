package se.webservices.WeatherForecast.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import se.webservices.WeatherForecast.models.Forecast;

@Repository
public interface ForecastRepository extends CrudRepository<Forecast, UUID>{


    @Override
    List<Forecast> findAll();


}