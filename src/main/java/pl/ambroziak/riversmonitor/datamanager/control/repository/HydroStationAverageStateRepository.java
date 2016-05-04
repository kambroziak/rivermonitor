package pl.ambroziak.riversmonitor.datamanager.control.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.ambroziak.riversmonitor.datamanager.entity.HydroStation;
import pl.ambroziak.riversmonitor.datamanager.entity.HydroStationAvarageState;

import java.util.List;

/**
 * Created by krzysztof on 30.04.16.
 */
public interface HydroStationAverageStateRepository extends CrudRepository<HydroStationAvarageState, Long> {

    @Query("SELECT m FROM HydroStationAvarageState m WHERE m.exported = ?1 AND (m.hydroStation = ?2 OR m.hydroStation = ?3) order by m.day")
    List<HydroStationAvarageState> findByExportedAndHydroStationOrderByDay(boolean exported, HydroStation hydroStation, HydroStation hydroStation2);

    HydroStationAvarageState findFirstByHydroStationOrderByDayDesc(HydroStation hydroStation);
}
