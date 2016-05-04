package pl.ambroziak.riversmonitor.datamanager.control.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.ambroziak.riversmonitor.datamanager.entity.HydroStation;
import pl.ambroziak.riversmonitor.datamanager.entity.Measurement;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by krzysztof on 30.04.16.
 */
public interface MeasurementRepository extends CrudRepository<Measurement,Long> {

    @Query("SELECT m FROM Measurement m WHERE m.measureTime >= ?1 and m.measureTime <= ?2 and m.hydroStation = ?3")
    List<Measurement> findByTimeRangeAndStation(LocalDateTime from, LocalDateTime to, HydroStation hydroStation);

    @Query("SELECT m FROM Measurement m WHERE m.measureTime >= ?1  and m.hydroStation = ?2")
    List<Measurement> findOlderThen(LocalDateTime from, HydroStation hydroStation);

    @Query("SELECT m FROM Measurement m order by m.measureTime")
    List<Measurement> findAll();

    Measurement findFirstByHydroStationOrderByMeasureTimeAsc(HydroStation hydroStation);

    Measurement findFirstByHydroStationOrderByMeasureTimeDesc(HydroStation hydroStation);

}
