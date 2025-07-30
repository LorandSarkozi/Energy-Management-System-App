package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.entities.Message;
import ro.tuc.ds2020.services.ChatService;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/chat")
public class ChatController {

    private final ChatService messageService;



    @Autowired
    public ChatController( ChatService messageService) {
        this.messageService = messageService;
    }


    @PostMapping("/messages")
    public ResponseEntity<?> sendMessage(@RequestBody Message message) {
        try {
            Message savedMessage = messageService.createMessage(message);
            return new ResponseEntity<>(savedMessage, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error sending message: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/messages/history/{senderId}/{receiverId}")
    public ResponseEntity<List<Message>> getChatHistory(@PathVariable UUID senderId, @PathVariable UUID receiverId) {
        List<Message> messages = messageService.getMessagesBetweenUsers(senderId, receiverId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
}
