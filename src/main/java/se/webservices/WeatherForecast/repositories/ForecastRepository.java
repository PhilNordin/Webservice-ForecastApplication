package se.webservices.WeatherForecast.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.webservices.WeatherForecast.models.Forecast;

import java.util.List;
import java.util.UUID;

@Repository
public interface ForecastRepository extends CrudRepository<Forecast, UUID> {
    @Override
    List<Forecast> findAll();




}
