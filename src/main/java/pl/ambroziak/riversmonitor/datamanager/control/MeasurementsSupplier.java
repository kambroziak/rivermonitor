package pl.ambroziak.riversmonitor.datamanager.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.ambroziak.riversmonitor.datamanager.entity.HydroStation;
import pl.ambroziak.riversmonitor.datamanager.entity.Measurement;
import pl.ambroziak.riversmonitor.datamanager.entity.rest.MeasurementData;
import pl.ambroziak.riversmonitor.datamanager.entity.rest.MeasurementRecord;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by krzysztof on 01.05.16.
 */
@Component
public class MeasurementsSupplier {

    @Autowired
    private HydroServiceClient hydroServiceClient;

    public List<Measurement> getAllAvailable(HydroStation hydroStation) {
        MeasurementData hydroStationMeasurement = hydroServiceClient.getHydroStationMeasurement(hydroStation.getRemoteId());
        return hydroStationMeasurement
                .getMeasurementRecord()
                .stream().map(record->toMeasurement(record,hydroStation))
                .collect(Collectors.toList());

    }

    private Measurement toMeasurement(MeasurementRecord measurementRecord, HydroStation hydroStation) {
        return new Measurement(measurementRecord.getDate(), measurementRecord.getValue().intValue(), hydroStation);
    }

}
