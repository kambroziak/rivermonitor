package pl.ambroziak.riversmonitor.datamanager.boundry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.ambroziak.riversmonitor.datamanager.control.HydroStationAvarageStateFacade;
import pl.ambroziak.riversmonitor.datamanager.control.MeasurementFacade;

/**
 * Created by krzysztof on 30.04.16.
 */
@Component
public class MeasurementsTimer {

    private static final Logger logger = Logger.getLogger(MeasurementsTimer.class);

    @Autowired
    private MeasurementFacade measurementFacade;

    @Autowired
    private HydroStationAvarageStateFacade avarageStateFacade;

    private static final int ONE_HOUR = 1000*60*60;

    @Scheduled(fixedDelay = ONE_HOUR)
    public void run() throws Exception {
        logger.info("MeasurementsTimer started work");
        measurementFacade.createNewMeasurmentsForAllNotecStations();
        avarageStateFacade.calculateAvarageStates();
        logger.info("MeasurementsTimer finished work");
    }

}
