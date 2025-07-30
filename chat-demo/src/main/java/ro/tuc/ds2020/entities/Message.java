package ro.tuc.ds2020.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Message {
    @Id
    @GeneratedValue
    private Long messageId;

    @Column(name = "sender_id")
    private UUID clientIdSender;

    @Column(name = "receiver_id")
    private UUID clientIdReceiver;

    private String content;
    private LocalDateTime timestamp;

    // Getters and setters
    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public UUID getClientIdSender() {
        return clientIdSender;
    }

    public void setClientIdSender(UUID clientIdSender) {
        this.clientIdSender = clientIdSender;
    }

    public UUID getClientIdReceiver() {
        return clientIdReceiver;
    }

    public void setClientIdReceiver(UUID clientIdReceiver) {
        this.clientIdReceiver = clientIdReceiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
