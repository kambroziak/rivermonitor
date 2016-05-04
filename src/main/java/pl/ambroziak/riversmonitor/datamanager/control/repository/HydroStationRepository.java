package pl.ambroziak.riversmonitor.datamanager.control.repository;

import org.springframework.data.repository.CrudRepository;
import pl.ambroziak.riversmonitor.datamanager.entity.HydroStation;

/**
 * Created by krzysztof on 30.04.16.
 */
public interface HydroStationRepository extends CrudRepository<HydroStation, Long> {

    HydroStation findByName(String name);

}
