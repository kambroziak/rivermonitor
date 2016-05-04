package pl.ambroziak.riversmonitor.datamanager.entity;

import javax.persistence.*;

/**
 * Created by krzysztof on 30.04.16.
 */
@Entity
@Table(name = "hydrostation")
public class HydroStation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hydrostation_id_seq")
    @SequenceGenerator(name = "hydrostation_id_seq", sequenceName = "hydrostation_id_seq", allocationSize = 1)
    private Long id;

    private Long remoteId;

    private String name;


    @OneToOne
    @JoinColumn(name = "stationupid")
    private HydroStation upHydroStation;

    @OneToOne
    @JoinColumn(name = "stationdownid")
    private HydroStation downHydroStation;

    protected HydroStation() {
    }

    public HydroStation(Long remoteId, String name, HydroStation downHydroStation) {
        this.remoteId = remoteId;
        this.name = name;
        this.downHydroStation = downHydroStation;
    }

    public Long getId() {
        return id;
    }

    public Long getRemoteId() {
        return remoteId;
    }

    public String getName() {
        return name;
    }


    public HydroStation getUpHydroStation() {
        return upHydroStation;
    }

    public HydroStation getDownHydroStation() {
        return downHydroStation;
    }

    public void setUpHydroStation(HydroStation upHydroStation) {
        this.upHydroStation = upHydroStation;
    }

    public void setDownHydroStation(HydroStation downHydroStation) {
        this.downHydroStation = downHydroStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HydroStation that = (HydroStation) o;

        return remoteId.equals(that.remoteId);

    }

    @Override
    public int hashCode() {
        return remoteId.hashCode();
    }
}
