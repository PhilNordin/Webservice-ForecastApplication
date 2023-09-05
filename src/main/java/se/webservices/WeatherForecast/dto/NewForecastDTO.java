package se.webservices.WeatherForecast.dto;



public class NewForecastDTO {  // DATA TRANSFER OBJECT
    private int date; //20230821
    private int hour;


    private float temperature;
    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

}

