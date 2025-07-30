package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
public class ReadController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Listen to the "/app/read/{personId}" topic
    @MessageMapping("/read/{personId}")
    public void sendReadStatus(String messageData, @DestinationVariable String personId) {
        // Assuming messageData contains the message ID and personId.
        // Parse messageData if needed
        System.out.println("Read status received: " + messageData);

        // Send the read status to the topic /topic/read/{personId} for the receiver to listen to
        messagingTemplate.convertAndSend("/topic/read/" + personId, messageData);
    }
}
