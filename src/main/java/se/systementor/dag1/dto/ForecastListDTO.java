package se.systementor.dag1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class ForecastListDTO {
    public UUID Id;
    public int Date; //20230821
    public int Hour;
    public float Temperature;

}
