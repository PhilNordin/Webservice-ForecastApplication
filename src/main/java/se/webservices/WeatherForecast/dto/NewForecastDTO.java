package se.webservices.WeatherForecast.dto;


import java.time.LocalDate;

public class NewForecastDTO {  // DATA TRANSFER OBJECT
    private LocalDate date; //20230821
    private int hour;
    private float temperature;


    //GET/SETTERS
    public LocalDate getDate() {
        return getDate();
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getTemperature() {
        return (int) temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

}

