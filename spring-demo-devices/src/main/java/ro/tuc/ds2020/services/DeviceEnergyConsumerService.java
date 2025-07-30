/*
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
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.entities.DeviceEnergyConsumption;
import ro.tuc.ds2020.repositories.DeviceEnergyConsumptionRepository;
import ro.tuc.ds2020.repositories.DeviceRepository;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Service
public class DeviceEnergyConsumerService {

    private static final String QUEUE_NAME = "energy_data_queue";
    private static final Map<String, LinkedList<Double>> deviceDataMap = new HashMap<>();

    private final DeviceEnergyConsumptionRepository repository;
    private final DeviceRepository deviceRepository;

    private Connection connection;
    private Channel channel;

    @Autowired
    public DeviceEnergyConsumerService(DeviceEnergyConsumptionRepository repository, DeviceRepository deviceRepository) {
        this.repository = repository;
        this.deviceRepository = deviceRepository;
    }

    @PostConstruct
    public void startConsuming() {
        new Thread(this::setupConsumer).start();
    }

    private void setupConsumer() {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println("Waiting for messages in " + QUEUE_NAME);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                JsonObject jsonMessage = new com.google.gson.JsonParser().parse(message).getAsJsonObject();

                String deviceId = jsonMessage.get("device_id").getAsString();
                double energyConsumption = jsonMessage.get("measurement_value").getAsDouble();

                System.out.println("Received message for device: " + deviceId + " with energy consumption: " + energyConsumption);

                deviceDataMap.computeIfAbsent(deviceId, k -> new LinkedList<>()).add(energyConsumption);

                if (deviceDataMap.get(deviceId).size() >= 6) {
                    double firstReading = deviceDataMap.get(deviceId).poll(); // Get the first reading and remove it
                    double sixthReading = deviceDataMap.get(deviceId).getLast(); // Get the 6th reading (last)

                    double hourlyConsumption = sixthReading - firstReading;
                    System.out.println("Hourly consumption for device " + deviceId + ": " + hourlyConsumption);

                    insertEnergyConsumption(deviceId, hourlyConsumption);

                    LinkedList<Double> updatedList = new LinkedList<>();
                    updatedList.add(sixthReading);
                    deviceDataMap.put(deviceId, updatedList);
                }
            };


            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
        } catch (Exception e) {
            e.printStackTrace();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/
