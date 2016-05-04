package pl.ambroziak.riversmonitor.googleconnector.boundry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.ambroziak.riversmonitor.datamanager.control.repository.HydroStationAverageStateRepository;
import pl.ambroziak.riversmonitor.datamanager.control.repository.HydroStationRepository;
import pl.ambroziak.riversmonitor.datamanager.entity.HydroStation;
import pl.ambroziak.riversmonitor.datamanager.entity.HydroStationAvarageState;
import pl.ambroziak.riversmonitor.googleconnector.control.GoogleConnectorFacade;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by krzysztof on 03.05.16.
 */
@Component
public class ExportToGoogleTimer {

    private static final int ONE_DAY = 1000 * 60 * 60 * 6;

    private static final Logger logger = Logger.getLogger(ExportToGoogleTimer.class);

    @Autowired
    private GoogleConnectorFacade googleConnectorFacade;

    @Autowired
    private HydroStationAverageStateRepository avarageStateRepository;

    @Autowired
    private HydroStationRepository hydroStationRepository;

    @Scheduled(fixedDelay = ONE_DAY)
    public void run() throws Exception {
        logger.info("Exporting to google");
        HydroStation czarnkow = hydroStationRepository.findByName("CZARNKÓW");
        HydroStation ujscie = hydroStationRepository.findByName("UJŚCIE");

        TreeMap<LocalDate, List<HydroStationAvarageState>> treeMap =
                new TreeMap<>((o1, o2) -> o1.compareTo(o2));

        treeMap.putAll(avarageStateRepository.findByExportedAndHydroStationOrderByDay(false, czarnkow, ujscie)
                .stream()
                .collect(Collectors.groupingBy(state -> state.getDay())));

        treeMap.entrySet().forEach(entry -> {
            try {
                exportData(entry);
            } catch (IOException e) {
                logger.error("Exception exporting data ", e);
            }
        });

    }

    private void exportData(Map.Entry<LocalDate, List<HydroStationAvarageState>> entry) throws IOException {
        List<HydroStationAvarageState> values =
                entry.getValue();

        Collections.sort(values, (o1, o2) -> o1
                .getHydroStation().getName().compareTo(o2.getHydroStation()
                        .getName()));


        Optional<HydroStationAvarageState> valueCzarnkow = values.isEmpty() ?
                Optional.empty() : Optional.of(values.get(0));

        Optional<HydroStationAvarageState> valueUjscie = values.size() < 2 ?
                Optional.empty() : Optional.of(values.get(1));

        googleConnectorFacade.export(entry.getKey(), valueCzarnkow, valueUjscie);

        values.stream().forEach(value -> {
            value.setExported(true);
            avarageStateRepository.save(value);
        });
    }
}
