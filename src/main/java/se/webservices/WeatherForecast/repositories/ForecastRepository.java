package se.webservices.WeatherForecast.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import se.webservices.WeatherForecast.models.ForeCast;

@Repository
public interface ForecastRepository extends CrudRepository<ForeCast, UUID>{


    @Override
    List<ForeCast> findAll();

    //List<ForeCast> findAllBy(String part);

}