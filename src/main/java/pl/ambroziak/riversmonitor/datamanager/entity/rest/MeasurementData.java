package pl.ambroziak.riversmonitor.datamanager.entity.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by krzysztof on 01.05.16.
 */
public class MeasurementData {

    @JsonProperty("waterStateRecords")
    private List<MeasurementRecord> measurementRecord;


    public MeasurementData() {
    }

    public List<MeasurementRecord> getMeasurementRecord() {
        return measurementRecord;
    }

    public void setMeasurementRecord(List<MeasurementRecord> measurementRecord) {
        this.measurementRecord = measurementRecord;
    }
}
