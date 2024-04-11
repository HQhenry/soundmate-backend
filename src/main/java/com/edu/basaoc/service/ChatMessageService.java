package com.edu.basaoc.service;

import com.edu.basaoc.model.entity.ChatMessage;
import com.edu.basaoc.model.entity.ChatRoom;
import com.edu.basaoc.model.entity.Profile;
import com.edu.basaoc.model.repository.ChatMessageRepository;
import com.edu.basaoc.model.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository, ChatRoomService chatRoomService,
                              ChatRoomRepository chatRoomRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomService = chatRoomService;
        this.chatRoomRepository = chatRoomRepository;
    }

    public ChatMessage save(ChatMessage chatMessage) {
        var chatId = chatRoomService.getChatRoomId(chatMessage.getSender().getProfileId(), chatMessage.getRecipient().getProfileId(), true)
                .orElseThrow();
        chatMessage.setChatId(chatId);
        chatMessageRepository.save(chatMessage);
        return chatMessage;

    }

    public List<ChatMessage> findChatMessages(long senderId, long recipientId) {

        var chatId = chatRoomService.getChatRoomId(senderId, recipientId, false)
                .orElseThrow();
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatId(chatId);
        if (chatMessages == null) {
            chatMessages = new ArrayList<>();
        }
        return chatMessages;
    }
}
