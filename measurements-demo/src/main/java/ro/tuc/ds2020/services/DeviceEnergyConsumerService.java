package ro.tuc.ds2020.services;

import com.rabbitmq.client.*;
import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.DeviceEnergyConsumption;
import ro.tuc.ds2020.repositories.DeviceEnergyConsumptionRepository;
import ro.tuc.ds2020.repositories.DeviceRepository;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class DeviceEnergyConsumerService {

    private static final String QUEUE_MEASUREMENTS = "energy_data_queue";
    private static final Map<String, LinkedList<Double>> deviceDataMap = Collections.synchronizedMap(new HashMap<>());

    private final DeviceEnergyConsumptionRepository repository;
    private final DeviceRepository deviceRepository;
    private final SimpMessagingTemplate messagingTemplate;

    private Connection connection;
    private Channel channel;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Autowired
    public DeviceEnergyConsumerService(DeviceEnergyConsumptionRepository repository,
                                       DeviceRepository deviceRepository,
                                       SimpMessagingTemplate messagingTemplate) {
        this.repository = repository;
        this.deviceRepository = deviceRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @PostConstruct
    public void startConsuming() {
        new Thread(this::setupConsumer).start();
    }

    private void setupConsumer() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbitmq");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_MEASUREMENTS, false, false, false, null);

            System.out.println("Waiting for messages in " + QUEUE_MEASUREMENTS);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                executorService.submit(() -> {
                    try {
                        processMessage(new String(delivery.getBody(), StandardCharsets.UTF_8));
                    } catch (Exception e) {
                        System.err.println("Error processing message: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
            };

            channel.basicConsume(QUEUE_MEASUREMENTS, true, deliverCallback, consumerTag -> {});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processMessage(String message) {
        JsonObject jsonMessage = new com.google.gson.JsonParser().parse(message).getAsJsonObject();

        String deviceId = jsonMessage.get("device_id").getAsString();
        double energyConsumption = jsonMessage.get("measurement_value").getAsDouble();

        System.out.println("Received message for device: " + deviceId + " with energy consumption: " + energyConsumption);

        synchronized (deviceDataMap) {
            // Add the new reading for this device
            deviceDataMap.computeIfAbsent(deviceId, k -> new LinkedList<>()).add(energyConsumption);

            // Only process if we have at least 6 readings for the device
            LinkedList<Double> readings = deviceDataMap.get(deviceId);
            if (readings.size() >= 6) {
                // Calculate hourly consumption
                double firstReading = readings.poll(); // Remove the first reading
                double sixthReading = readings.getLast(); // Get the 6th reading

                double hourlyConsumption = sixthReading - firstReading;
                System.out.println("Hourly consumption for device " + deviceId + ": " + hourlyConsumption);

                // Process device-specific logic
                Optional<Device> device = deviceRepository.findById(UUID.fromString(deviceId));
                if (device.isPresent()) {
                    Device currentDevice = device.get();

                    // Check if hourly consumption exceeds the maximum limit
                    if (currentDevice.getMaximumHourlyEnergyConsumption() < hourlyConsumption) {
                        System.out.println("Hourly consumption for device " + deviceId + " is too high");

                        // Send WebSocket notification
                        UUID personId = currentDevice.getPersonId();
                        if (personId != null) {
                            String notification = "[WARNING] Device " + deviceId +
                                    " exceeded its hourly energy limit at " + LocalDateTime.now() + "!";
                            messagingTemplate.convertAndSend("/topic/alerts/" + personId, notification);
                            System.out.println("Notification sent to personId " + personId);
                        }
                    }
                }

                // Save the hourly consumption in the database
                insertEnergyConsumption(deviceId, hourlyConsumption);

                // Retain only the last reading for further processing
                LinkedList<Double> updatedList = new LinkedList<>();
                updatedList.add(sixthReading);
                deviceDataMap.put(deviceId, updatedList);
            }
        }
    }

    private void insertEnergyConsumption(String deviceId, double hourlyConsumption) {
        DeviceEnergyConsumption deviceEnergyConsumption = new DeviceEnergyConsumption();
        deviceEnergyConsumption.setDeviceId(deviceId);
        deviceEnergyConsumption.setHourlyConsumption(hourlyConsumption);
        deviceEnergyConsumption.setTimestamp(LocalDateTime.now());

        repository.save(deviceEnergyConsumption);
        System.out.println("Inserted hourly consumption for device " + deviceId + " into database.");
    }

    @PreDestroy
    public void cleanup() {
        try {
            if (channel != null) {
                channel.close();
            }
            if (connection != null) {
                connection.close();
            }
            executorService.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

