package se.webservices.WeatherForecast.services.data;

import java.util.Date;
import java.util.List;

public class Root {
    private Date approvedTime;
    private Date referenceTime;
    private Geometry geometry;
    private List<TimeSeries> timeSeries;

    public List<TimeSeries> getTimeSeries() {
        return timeSeries;
    }

    public void setTimeSeries(List<TimeSeries> timeSeries) {
        this.timeSeries = timeSeries;
    }
    public void setApprovedTime(Date approvedTime) {
        this.approvedTime = approvedTime;
    }
    public void setReferenceTime(Date referenceTime) {
        this.referenceTime = referenceTime;
    }
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public List<TimeSeries> getTimeseries() {
        return timeSeries;
    }
    public Geometry getGeometry() {
        return geometry;
    }
    public Date getApprovedTime() {
        return approvedTime;
    }
    public Date getReferenceTime() {
        return referenceTime;
    }
}