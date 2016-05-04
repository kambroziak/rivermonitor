package pl.ambroziak.riversmonitor.datamanager.entity.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by krzysztof on 30.04.16.
 */
public class HydroStationData {

    protected HydroStationData(){
    }

    private Long id;

    private String name;

    @JsonProperty("connections")
    private HydroStationConnection hydroStationConnection;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public HydroStationConnection getHydroStationConnection() {
        return hydroStationConnection;
    }
}
