package ro.tuc.ds2020.services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.repositories.DeviceRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    private static final String QUEUE_CREATE = "device_create";

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @PostConstruct
    public void startConsuming() {
        new Thread(this::startListeningToQueue).start();
    }


    public void startListeningToQueue() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbitmq");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");

        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            // Declare the queue
            channel.queueDeclare(QUEUE_CREATE, false, false, false, null);

            System.out.println("Listening for messages on queue: " + QUEUE_CREATE);

            // Define the callback to handle incoming messages
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

                try {
                    // Parse the message and create a device
                    JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
                    Device device = createDevice(jsonObject);
                    System.out.println("Device created: " + device.getDeviceId());
                } catch (Exception e) {
                    System.err.println("Failed to process message: " + e.getMessage());
                }
            };

            channel.basicConsume(QUEUE_CREATE, true, deliverCallback, consumerTag -> {
            });

        } catch (IOException | TimeoutException e) {
            throw new RuntimeException("Error setting up RabbitMQ listener: " + e.getMessage(), e);
        }
    }

    public Device createDevice(JsonObject jsonObject) {
        UUID deviceId = UUID.fromString(jsonObject.get("deviceId").getAsString());
        Double maxHourlyConsumption = jsonObject.get("maxHourlyConsumption").getAsDouble();

        UUID personId = null;
        if (jsonObject.has("personId") && !jsonObject.get("personId").isJsonNull()) {
            String personIdString = jsonObject.get("personId").getAsString();
            if (!"null".equalsIgnoreCase(personIdString)) { // Handle invalid "null" string
                personId = UUID.fromString(personIdString);
            }
        }

        Device device = new Device(deviceId, maxHourlyConsumption, personId);
        return deviceRepository.save(device);
    }
}
