package pl.ambroziak.riversmonitor.datamanager.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by krzysztof on 30.04.16.
 */
@Entity
@Table(name = "measurement")
public class Measurement {



    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "measuerement_id_seq")
    @SequenceGenerator(name = "measuerement_id_seq", sequenceName = "measuerement_id_seq")
    private Long id;

    private LocalDateTime measureTime;

    private LocalDateTime createTime;

    private Integer value;

    @ManyToOne
    @JoinColumn(name = "hydrostationid")
    private HydroStation hydroStation;

    @PrePersist
    public void preupdate() {
        createTime = LocalDateTime.now();
    }

    public Measurement(LocalDateTime measureTime, Integer value, HydroStation station) {
        this.measureTime = measureTime;
        this.value = value;
        this.hydroStation = station;
    }

    public Measurement() {
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getMeasureTime() {
        return measureTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Measurement that = (Measurement) o;

        if (!measureTime.equals(that.measureTime)) return false;
        if (!value.equals(that.value)) return false;
        return hydroStation.equals(that.hydroStation);

    }

    @Override
    public int hashCode() {
        int result = measureTime.hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + hydroStation.hashCode();
        return result;
    }
}
