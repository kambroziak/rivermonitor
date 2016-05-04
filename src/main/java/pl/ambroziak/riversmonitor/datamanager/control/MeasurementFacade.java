package pl.ambroziak.riversmonitor.datamanager.control;

import com.google.common.collect.Iterables;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.ambroziak.riversmonitor.datamanager.control.repository.HydroStationRepository;
import pl.ambroziak.riversmonitor.datamanager.control.repository.MeasurementRepository;
import pl.ambroziak.riversmonitor.datamanager.entity.HydroStation;
import pl.ambroziak.riversmonitor.datamanager.entity.Measurement;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by krzysztof on 01.05.16.
 */
@Component
public class MeasurementFacade {

    @Autowired
    private HydroStationRepository hydroStationRepository;

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private MeasurementsSupplier measurementsSupplier;

    private static final Logger logger = Logger.getLogger(MeasurementFacade.class);

    public void createNewMeasurmentsForAllNotecStations() throws ExecutionException, InterruptedException {
        Iterable<HydroStation> stations = hydroStationRepository.findAll();
        System.out.println(Iterables.size(stations));
        CompletableFuture[] tasks = (CompletableFuture[])StreamSupport.stream(stations.spliterator(), false)
                .map(hydroStation -> CompletableFuture.runAsync(getData(hydroStation))
                ).toArray(size->new CompletableFuture[size]);

        logger.info("Executing tasks");
        CompletableFuture.allOf(tasks).get();
        logger.info("Tasks executed");
    }

    private Runnable getData(HydroStation hydroStation) {
        return () -> {
                    logger.info("Getting data for station: "+hydroStation.getName());
                    List<Measurement> allAvailable = getAllAvaliableMeasurements(hydroStation);
                    List<Measurement> olderThen = measurementRepository.findOlderThen(allAvailable.get(0).getMeasureTime(), hydroStation);
                    allAvailable.removeAll(olderThen);
                    logger.info("Saving "+allAvailable.size()+" records");
                    measurementRepository.save(allAvailable);
                };
    }

    private List<Measurement> getAllAvaliableMeasurements(HydroStation hydroStation) {
        return measurementsSupplier.getAllAvailable(hydroStation)
                .stream()
                .sorted(Comparator.comparing(Measurement::getMeasureTime))
                .collect(Collectors.toList());
    }
}
