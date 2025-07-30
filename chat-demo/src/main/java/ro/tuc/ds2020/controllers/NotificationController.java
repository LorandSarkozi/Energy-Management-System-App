package ro.tuc.ds2020.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Handle messages sent to /app/chat/{receiverId}
    @MessageMapping("/chat/{receiverId}")
    public void sendMessage(String message, @org.springframework.messaging.handler.annotation.DestinationVariable String receiverId) {
        // Forward the message to /topic/chat/{receiverId}
        messagingTemplate.convertAndSend("/topic/chat/" + receiverId, message);
    }
}
