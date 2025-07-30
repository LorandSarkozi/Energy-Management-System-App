package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.tuc.ds2020.entities.DeviceEnergyConsumption;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface DeviceEnergyConsumptionRepository extends JpaRepository<DeviceEnergyConsumption, Long> {

    List<DeviceEnergyConsumption> findByDeviceIdAndTimestampBetween(
            String deviceId,
            LocalDateTime start,
            LocalDateTime end
    );




}