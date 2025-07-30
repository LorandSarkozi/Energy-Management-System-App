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
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID deviceId;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "max_hourly_energy_consumption", nullable = false)
    private Double maximumHourlyEnergyConsumption;

    @ManyToOne(optional = true)
    @JoinColumn(name = "person_id")
    private Person person;

    public Device(String description, UUID deviceId, String address, Double maximumHourlyEnergyConsumption, Person person) {
        this.description = description;
        this.deviceId = deviceId;
        this.address = address;
        this.maximumHourlyEnergyConsumption = maximumHourlyEnergyConsumption;
        this.person = person;
    }

    public Device(String description, UUID deviceId, String address, Double maximumHourlyEnergyConsumption) {
        this.description = description;
        this.deviceId = deviceId;
        this.address = address;
        this.maximumHourlyEnergyConsumption = maximumHourlyEnergyConsumption;
    }
}
