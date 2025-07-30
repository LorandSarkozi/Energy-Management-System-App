package ro.tuc.ds2020.controllers;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ro.tuc.ds2020.entities.TypingStatus;


@Controller
public class TypingController {

    private final SimpMessagingTemplate messagingTemplate;

    public TypingController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Handle typing status messages sent to /app/typing/{receiverId}
    @MessageMapping("/typing/{receiverId}")
    public void handleTypingStatus(@Payload TypingStatus typingStatus, @DestinationVariable String receiverId) {
        // Forward the typing status to the corresponding topic
        messagingTemplate.convertAndSend("/topic/typing/" + receiverId, typingStatus);
    }
}
