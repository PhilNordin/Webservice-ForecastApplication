package se.systementor.dag1.services.smhi.data;

import java.util.Date;
import java.util.List;

public class TimeSeries {
    private Date validTime;
    private List<Parameter> parameters;
    private Geometry geometry;

    public Date getValidTime(){
        return validTime;
    }

    public List<Parameter> getParameters(){
        return parameters;
    }

    public Geometry getGeometry(){
        return geometry;
    }
}
