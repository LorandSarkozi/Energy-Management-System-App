package ro.tuc.ds2020.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class DeviceEnergyConsumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment primary key
    private Long id;

    private String deviceId;
    private Double hourlyConsumption;
    private LocalDateTime timestamp;

    public DeviceEnergyConsumption(String deviceId, Double hourlyConsumption, LocalDateTime timestamp) {
        this.deviceId = deviceId;
        this.hourlyConsumption = hourlyConsumption;
        this.timestamp = timestamp;
    }
}
