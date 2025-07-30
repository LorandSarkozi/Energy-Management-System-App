package ro.tuc.ds2020.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.entities.Message;
import ro.tuc.ds2020.repositories.MessageRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ChatService {

    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatService(MessageRepository messageRepository, SimpMessagingTemplate messagingTemplate) {
        this.messageRepository = messageRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public Message createMessage(Message message) {

        Message createdMessage = new Message();
        createdMessage.setMessageId(message.getMessageId());
        createdMessage.setClientIdSender(message.getClientIdSender());
        createdMessage.setClientIdReceiver(message.getClientIdReceiver());
        createdMessage.setContent(message.getContent());
        createdMessage.setTimestamp(LocalDateTime.now());

        messageRepository.save(createdMessage);

        sendWebSocketMessageToReceiver(createdMessage);

        return createdMessage;
    }

    private void sendWebSocketMessageToReceiver(Message message) {
        // Construct the WebSocket destination based on the receiverId
        String destination = "/topic/chat/" + message.getClientIdReceiver();
        // Send the message to the receiver via WebSocket
        messagingTemplate.convertAndSend(destination, message);
    }


    public List<Message> getMessagesBetweenUsers(UUID senderId, UUID receiverId) {
        return messageRepository.findMessagesBetweenUsers(senderId, receiverId);
    }

}
