package ro.tuc.ds2020.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.tuc.ds2020.entities.Message;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE " +
            "(m.clientIdSender = :senderId AND m.clientIdReceiver = :receiverId) " +
            "OR (m.clientIdSender = :receiverId AND m.clientIdReceiver = :senderId) " +
            "ORDER BY m.timestamp ASC")
    List<Message> findMessagesBetweenUsers(
            @Param("senderId") UUID senderId,
            @Param("receiverId") UUID receiverId);
}