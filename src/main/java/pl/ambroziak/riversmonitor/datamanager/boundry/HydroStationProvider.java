package pl.ambroziak.riversmonitor.datamanager.boundry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.ambroziak.riversmonitor.datamanager.control.HydroServiceClient;
import pl.ambroziak.riversmonitor.datamanager.control.repository.HydroStationRepository;
import pl.ambroziak.riversmonitor.datamanager.entity.HydroStation;
import pl.ambroziak.riversmonitor.datamanager.entity.rest.HydroStationData;
import pl.ambroziak.riversmonitor.datamanager.entity.rest.HydroUpId;

/**
 * Created by krzysztof on 30.04.16.
 */
@Component
public class HydroStationProvider {

    private static final Long FIRST_STATION_ID = 152150090l;

    @Autowired
    private HydroServiceClient hydroServiceClient;

    @Autowired
    private HydroStationRepository hydroStationRepository;

    public void insertHydroStationIntoDb() {
        HydroStation down = null;
        HydroStationData hydroStationData = getHydroStationData(FIRST_STATION_ID);

        while (getNextStation(hydroStationData) != null) {

            HydroStation hydroStation = saveHydroStation(down, hydroStationData);
            down = hydroStation;
            hydroStationData = getHydroStationData(getNextStation(hydroStationData).getHydroUpId());
        }
        saveHydroStation(down,hydroStationData);

    }

    private HydroStation saveHydroStation(HydroStation down, HydroStationData hydroStationData) {
        HydroStation hydroStation = new HydroStation(hydroStationData.getId(), hydroStationData.getName(),
                down);
        hydroStationRepository.save(hydroStation);
        if (down != null) {
            setUpStation(down, hydroStation);
        }
        return hydroStation;
    }

    private HydroUpId getNextStation(HydroStationData hydroStationData) {
        return hydroStationData.getHydroStationConnection().getHydroUpId();
    }

    private HydroStationData getHydroStationData(Long stationId) {
        return hydroServiceClient
                .getHydroStationData(stationId);
    }

    private void setUpStation(HydroStation down, HydroStation hydroStation) {
        down.setUpHydroStation(hydroStation);
        hydroStationRepository.save(down);
    }
}