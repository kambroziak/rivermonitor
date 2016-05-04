package pl.ambroziak.riversmonitor.datamanager.entity.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by krzysztof on 30.04.16.
 */
public class HydroStationConnection {

    @JsonProperty(value = "hydroUp")
    private HydroUpId hydroUpId;

    public HydroStationConnection() {
    }

    public HydroUpId getHydroUpId() {
        return hydroUpId;
    }
}
