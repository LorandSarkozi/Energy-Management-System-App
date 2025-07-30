package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.entities.DeviceEnergyConsumption;
import ro.tuc.ds2020.repositories.DeviceEnergyConsumptionRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/energy")
public class EnergyConsumptionController {

    @Autowired
    private DeviceEnergyConsumptionRepository repository;

    @GetMapping("/consumption/{deviceId}")
    public List<DeviceEnergyConsumption> getConsumptionByDeviceAndDate(
            @PathVariable String deviceId,
            @RequestParam String date) { // Pass date as "yyyy-MM-dd"

        // Parse the provided date into LocalDate
        LocalDate selectedDate = LocalDate.parse(date);
        LocalDateTime startOfDay = selectedDate.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        // Call the repository method
        return repository.findByDeviceIdAndTimestampBetween(deviceId, startOfDay, endOfDay);
    }
}
