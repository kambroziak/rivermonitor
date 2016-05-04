package pl.ambroziak.riversmonitor.datamanager.entity.rest;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import pl.ambroziak.riversmonitor.datamanager.control.converters.LocalDateTimeDeserializer;

import java.time.LocalDateTime;

/**
 * Created by krzysztof on 01.05.16.
 */
public class MeasurementRecord {

    private Float value;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime date;

    public MeasurementRecord() {
    }

    public Float getValue() {
        return value;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
