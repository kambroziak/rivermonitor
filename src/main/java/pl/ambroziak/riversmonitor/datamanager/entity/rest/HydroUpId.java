package pl.ambroziak.riversmonitor.datamanager.entity.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by krzysztof on 30.04.16.
 */
public class HydroUpId {

    @JsonProperty("item1")
    private Long hydroUpId;

    public HydroUpId() {
    }
    public Long getHydroUpId() {
        return hydroUpId;
    }
}
