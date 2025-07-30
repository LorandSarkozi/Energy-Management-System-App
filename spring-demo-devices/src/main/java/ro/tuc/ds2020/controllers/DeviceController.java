package ro.tuc.ds2020.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.DeviceDto;
import ro.tuc.ds2020.services.DeviceService;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(/*value = "/api/admin"*/)
public class DeviceController {

    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping("/devices")
    public ResponseEntity<DeviceDto> createDevice(@RequestBody DeviceDto deviceDto) {

        DeviceDto createdDevice = deviceService.createDevice(deviceDto);
        return ResponseEntity.ok(createdDevice);
    }

    // Retrieve all devices
    @GetMapping("/devices")
    public ResponseEntity<List<DeviceDto>> getAllDevices() {
        List<DeviceDto> devices = deviceService.getAllDevices();
        return ResponseEntity.ok(devices);
    }

    // Retrieve a single device by ID
    @GetMapping("/devices/{deviceId}")
    public ResponseEntity<DeviceDto> getDeviceById(@PathVariable UUID deviceId) {
        DeviceDto device = deviceService.getDeviceById(deviceId);
        return ResponseEntity.ok(device);
    }

    // Update a device by ID
    @PutMapping("/devices/{deviceId}")
    public ResponseEntity<DeviceDto> updateDevice(@PathVariable UUID deviceId, @RequestBody DeviceDto updatedDeviceDto) {
        DeviceDto updatedDevice = deviceService.updateDevice(deviceId, updatedDeviceDto);
        return ResponseEntity.ok(updatedDevice);
    }

    // Delete a device by ID
    @DeleteMapping("/devices/{deviceId}")
    public ResponseEntity<Void> deleteDevice(@PathVariable UUID deviceId) {
        deviceService.deleteDevice(deviceId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/devices/find/{personId}")
    public ResponseEntity<List<DeviceDto>> getDevicesByCustomerId(@PathVariable UUID personId) {
        List<DeviceDto> devices = deviceService.findDevicesByPersonId(personId);
        return ResponseEntity.ok(devices);
    }


}
