package pl.ambroziak.riversmonitor.datamanager.entity;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by krzysztof on 30.04.16.
 */
@Entity
@Table(name = "hydrostationavaragestate")
public class HydroStationAvarageState {

    @Id
    @GeneratedValue
    @SequenceGenerator(name = "hydrostationavaragestate_id_seq",sequenceName = "hydrostationavaragestate_id_seq", allocationSize = 1)
    private Long id;

    private LocalDate day;

    private Double value;

    private boolean exported;

    @OneToOne
    @JoinColumn(name = "hydrostationid")
    private HydroStation hydroStation;

    public HydroStationAvarageState() {
    }

    public HydroStationAvarageState(LocalDate day, Double value, HydroStation hydroStation) {
        this.day = day;
        this.value = value;
        this.hydroStation = hydroStation;
    }

    public LocalDate getDay() {
        return day;
    }

    public Double getValue() {
        return value;
    }

    public HydroStation getHydroStation() {
        return hydroStation;
    }

    public void setExported(boolean exported) {
        this.exported = exported;
    }
}
