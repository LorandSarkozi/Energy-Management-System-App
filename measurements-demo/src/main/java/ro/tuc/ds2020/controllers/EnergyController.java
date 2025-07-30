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
public class EnergyController {

    @Autowired
    private DeviceEnergyConsumptionRepository repository;

    @GetMapping("/consumption/{deviceId}")
    public List<DeviceEnergyConsumption> getConsumptionByDeviceAndDate(
            @PathVariable String deviceId,
            @RequestParam String date) {

        // Trim the date input to ensure no extraneous characters
        LocalDate selectedDate = LocalDate.parse(date.trim());
        LocalDateTime startOfDay = selectedDate.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        return repository.findByDeviceIdAndTimestampBetween(deviceId, startOfDay, endOfDay);
    }
}
