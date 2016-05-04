package pl.ambroziak.riversmonitor.datamanager.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.ambroziak.riversmonitor.datamanager.control.repository.HydroStationAverageStateRepository;
import pl.ambroziak.riversmonitor.datamanager.control.repository.HydroStationRepository;
import pl.ambroziak.riversmonitor.datamanager.control.repository.MeasurementRepository;
import pl.ambroziak.riversmonitor.datamanager.entity.HydroStation;
import pl.ambroziak.riversmonitor.datamanager.entity.HydroStationAvarageState;
import pl.ambroziak.riversmonitor.datamanager.entity.Measurement;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by krzysztof on 02.05.16.
 */
@Component
public class HydroStationAvarageStateFacade {

    @Autowired
    private HydroStationAverageStateCalculator calculator;

    @Autowired
    private HydroStationRepository hydroStationRepository;

    @Autowired
    private HydroStationAverageStateRepository hydroStationAvarageStateRepository;

    @Autowired
    private MeasurementRepository measurementRepository;

    public void calculateAvarageStates() {
        StreamSupport.stream(hydroStationRepository.findAll().spliterator(), true)
                .forEach(this::calculateAvarageStates);
    }

    private void calculateAvarageStates(HydroStation hydroStation) {
        Optional<HydroStationAvarageState> lastAvarageState = Optional.ofNullable(hydroStationAvarageStateRepository.findFirstByHydroStationOrderByDayDesc(hydroStation));
        List<LocalDate> datesToCalculateAvarage = getDatesToCalculateAvarage(lastAvarageState,hydroStation);
        calculateAvarageStateForDate(hydroStation, datesToCalculateAvarage);
    }

    private void calculateAvarageStateForDate(HydroStation hydroStation, List<LocalDate> datesToCalculateAvarage) {
        datesToCalculateAvarage
                .stream()
                .map(date -> calculator.calculateAverageState(hydroStation, date))
                .forEach(hydroStationAvarageStateRepository::save);
    }

    private List<LocalDate> getDatesToCalculateAvarage(Optional<HydroStationAvarageState> lastAvarageState, HydroStation hydroStation) {
        LocalDate to = Optional.ofNullable(measurementRepository.findFirstByHydroStationOrderByMeasureTimeDesc(hydroStation))
                .orElseThrow(()->new IllegalStateException("No data avaliable"))
                .getMeasureTime()
                .minusDays(1)
                .toLocalDate();

        LocalDate dayFrom = lastAvarageState.isPresent() ?
                lastAvarageState.get().getDay() : getFirstMeasureDate(hydroStation)
                .minusDays(1);

        return Stream.iterate(dayFrom.plusDays(1), prev -> prev.plusDays(1l))
                .limit(dayFrom.until(to).getDays())
                .collect(Collectors.toList());
    }

    private LocalDate getFirstMeasureDate(HydroStation hydroStation){
        Measurement first = measurementRepository.findFirstByHydroStationOrderByMeasureTimeAsc(hydroStation);
        if(first!=null){
            return first.getMeasureTime().toLocalDate();
        }
        throw new IllegalStateException("No data to calculate avarage!");
    }

}
