package pl.ambroziak.riversmonitor.datamanager.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.ambroziak.riversmonitor.datamanager.entity.rest.HydroStationData;
import pl.ambroziak.riversmonitor.datamanager.entity.rest.MeasurementData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by krzysztof on 30.04.16.
 */
@Component
public class HydroServiceClient {

    @Autowired
    private RestTemplate template;

    private static final String URL = "http://monitor.pogodynka.pl/api/station/hydro/?id={id}";

    public HydroStationData getHydroStationData(Long id) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("id", id);
        System.out.println(template.getForObject(URL, String.class, variables));
        return template.getForObject(URL, HydroStationData.class, variables);

    }

    public MeasurementData getHydroStationMeasurement(Long id) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("id", id);
        return template.getForObject(URL, MeasurementData.class, variables);


    }
}
