package ro.tuc.ds2020.services;

import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.DeviceDto;
import ro.tuc.ds2020.dtos.builders.DeviceBuilder;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.Person;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.repositories.PersonRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    private static final String QUEUE_CREATE = "device_create";
    private static final String QUEUE_UPDATE = "device_update";
    private static final String QUEUE_DELETE = "device_delete";

    private final DeviceRepository deviceRepository;
    private final PersonRepository personRepository;

    private Connection connection;
    private Channel channel;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, PersonRepository personRepository) {
        this.deviceRepository = deviceRepository;
        this.personRepository = personRepository;
    }

    public DeviceDto createDevice(DeviceDto deviceDto) {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbitmq");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_CREATE, false, false, false, null);

            Device device = DeviceBuilder.toEntity(deviceDto); // Convert DTO to Entity


            Person person = personRepository.findByPersonId(deviceDto.getPersonId());
            System.out.println(deviceDto.getPersonId());
            if (deviceDto.getPersonId() != null && person !=null) {

                device.setPerson(person);
            } else {
                device.setPerson(null);
            }

            // Save the device in the repository
            device = deviceRepository.save(device);

            JsonObject message = new JsonObject();
            message.addProperty("deviceId", String.valueOf(device.getDeviceId()));
            message.addProperty("maxHourlyConsumption", device.getMaximumHourlyEnergyConsumption());
            message.addProperty("personId", String.valueOf(device.getPerson() != null ? device.getPerson().getPersonId() : null));

            channel.basicPublish("", QUEUE_CREATE, null, message.toString().getBytes(StandardCharsets.UTF_8));
            System.out.println("Sent: " + message);

            return DeviceBuilder.toDeviceDto(device); // Convert the saved entity back to DTO

        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }

    }

    public List<DeviceDto> getAllDevices() {
        return deviceRepository.findAll().stream()
                .map(DeviceBuilder::toDeviceDto)
                .collect(Collectors.toList());
    }

    public DeviceDto getDeviceById(UUID deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device with ID " + deviceId + " not found"));
        return DeviceBuilder.toDeviceDto(device);
    }

    public DeviceDto updateDevice(UUID deviceId, DeviceDto updatedDeviceDto) {
        // Find the existing device by ID or throw an exception if it doesn't exist

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");


        Device existingDevice = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device with ID " + deviceId + " not found"));

        // Update the fields of the existing device from the updatedDeviceDto
        existingDevice.setDescription(updatedDeviceDto.getDescription());
        existingDevice.setAddress(updatedDeviceDto.getAddress());
        existingDevice.setMaximumHourlyEnergyConsumption(updatedDeviceDto.getMaximumHourlyEnergyConsumption());

        // Update the person association if a person ID is provided
        if (updatedDeviceDto.getPersonId() != null) {
            Person person = personRepository.findByPersonId(updatedDeviceDto.getPersonId());
            if (person != null) {
                existingDevice.setPerson(person);
            } else {
                throw new ResourceNotFoundException("Person with ID " + updatedDeviceDto.getPersonId() + " not found");
            }
        } else {
            existingDevice.setPerson(null); // Clear the person association if no person ID is provided
        }

        // Save the updated device to the repository
        deviceRepository.save(existingDevice);

        // Return the updated device as a DTO
        return DeviceBuilder.toDeviceDto(existingDevice);
    }


    public void deleteDevice(UUID deviceId) {


        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");

        if (!deviceRepository.existsById(deviceId)) {
            throw new ResourceNotFoundException("Device with ID " + deviceId + " not found");
        }
        deviceRepository.deleteById(deviceId);
    }

    public List<DeviceDto> findDevicesByPersonId(UUID personId) {
        // Implement the logic to retrieve devices based on the customerId
        Person person = personRepository.findByPersonId(personId);

        List<Device> devices = deviceRepository.findByPerson(person);
        return devices.stream().map(DeviceBuilder::toDeviceDto).collect(Collectors.toList());
    }


}
