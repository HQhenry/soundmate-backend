package com.edu.basaoc.model.repository;

import com.edu.basaoc.model.entity.ChatMessage;
import com.edu.basaoc.model.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByChatId(String chatId);


}
