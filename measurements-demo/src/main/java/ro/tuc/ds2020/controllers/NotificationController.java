package ro.tuc.ds2020.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api")
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/test-notification")
    public void sendTestNotification(@RequestParam String personId, @RequestParam String message) {
        messagingTemplate.convertAndSend("/topic/alerts/" + personId, message);
        System.out.println("Notification sent to /topic/alerts/" + personId + ": " + message);
    }
}