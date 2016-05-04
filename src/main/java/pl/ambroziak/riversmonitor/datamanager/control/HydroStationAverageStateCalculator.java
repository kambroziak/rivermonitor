package pl.ambroziak.riversmonitor.datamanager.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.ambroziak.riversmonitor.datamanager.control.repository.MeasurementRepository;
import pl.ambroziak.riversmonitor.datamanager.entity.HydroStation;
import pl.ambroziak.riversmonitor.datamanager.entity.HydroStationAvarageState;
import pl.ambroziak.riversmonitor.datamanager.entity.Measurement;

import java.time.LocalDate;
import java.util.OptionalDouble;

/**
 * Created by krzysztof on 30.04.16.
 */
@Component
public class HydroStationAverageStateCalculator {

    @Autowired
    private MeasurementRepository measurementRepository;

    public HydroStationAvarageState calculateAverageState(HydroStation station, LocalDate day) {
        OptionalDouble average = measurementRepository.findByTimeRangeAndStation(day.atStartOfDay(),
                day.atTime(23, 59, 59), station)
                .stream()
                .mapToInt(Measurement::getValue)
                .average();
        System.out.print("Day: "+day);
        System.out.print("station: "+station.getName());
        return new HydroStationAvarageState(day, average.getAsDouble(), station);
    }

}
