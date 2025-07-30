package ro.tuc.ds2020.entities;



import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Data
@RequiredArgsConstructor

@Table(name="device")
public class Device implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID deviceId;


    @Column(name = "max_hourly_energy_consumption", nullable = false)
    private Double maximumHourlyEnergyConsumption;

    @JoinColumn(name = "person_id")
    private UUID personId;

    public Device( UUID deviceId,  Double maximumHourlyEnergyConsumption, UUID personId) {
        this.deviceId = deviceId;
        this.maximumHourlyEnergyConsumption = maximumHourlyEnergyConsumption;
        this.personId = personId;
    }

    public Device( UUID deviceId, Double maximumHourlyEnergyConsumption) {
        this.deviceId = deviceId;
        this.maximumHourlyEnergyConsumption = maximumHourlyEnergyConsumption;
    }
}
