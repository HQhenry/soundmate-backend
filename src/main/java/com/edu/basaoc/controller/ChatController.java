package com.edu.basaoc.controller;

import com.edu.basaoc.model.ChatMessageRequestDto;
import com.edu.basaoc.model.ChatMessageResponseDto;
import com.edu.basaoc.model.ChatRoomResponseDto;
import com.edu.basaoc.model.entity.*;
import com.edu.basaoc.model.mapper.ChatMessageMapper;
import com.edu.basaoc.model.mapper.ProfileResponseDtoMapper;
import com.edu.basaoc.service.AccountService;
import com.edu.basaoc.service.ChatMessageService;
import com.edu.basaoc.service.ChatRoomService;
import com.edu.basaoc.service.ProfileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import net.bytebuddy.dynamic.DynamicType;
import org.hibernate.annotations.Any;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin
public class ChatController {
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate messagingTemplate;

    private final AccountService accountService;
    private final ProfileService profileService;

   // private final ChatMessageMapper chatMessageMapper = Mappers.getMapper(ChatMessageMapper.class);
    public ChatController(ChatMessageService chatMessageService, ChatRoomService chatRoomService, SimpMessagingTemplate messagingTemplate, AccountService accountService, ProfileService profileService) {
        this.chatMessageService = chatMessageService;
        this.chatRoomService = chatRoomService;
        this.messagingTemplate = messagingTemplate;
        this.accountService = accountService;
        this.profileService = profileService;
    }

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageRequestDto chatMessageRequestDto) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent(chatMessageRequestDto.getContent());
        chatMessage.setSender(profileService.getProfileById(chatMessageRequestDto.getSender()));
        chatMessage.setRecipient(profileService.getProfileById(chatMessageRequestDto.getRecipient()));
        chatMessage.setChatId("2_1");
        chatMessage.setTimestamp(new Timestamp(System.currentTimeMillis()));
       ChatMessage savedMessage = chatMessageService.save(chatMessage);

        ChatNotification chatNotification = new ChatNotification();
        chatNotification.setChatId(savedMessage.getChatId());
        chatNotification.setSenderId(Long.toString(savedMessage.getSender().getProfileId()));
        chatNotification.setRecipientId(Long.toString(savedMessage.getRecipient().getProfileId()));
        chatNotification.setContent(savedMessage.getContent());
        chatNotification.setTimestamp(savedMessage.getTimestamp());
        chatNotification.setChatMessageId(savedMessage.getChatMessageId().toString());

       messagingTemplate.convertAndSendToUser(
               Long.toString(chatMessage.getRecipient().getProfileId()),
               "queue/messages",
               chatNotification);

    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessageResponseDto>> findChatMessages(
            @PathVariable("senderId") long senderId,
            @PathVariable("recipientId") long recipientId
    ) {
        List<ChatMessage> chatMessages = chatMessageService.findChatMessages(senderId, recipientId);
        if (chatMessages == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<ChatMessageResponseDto> chatMessageResponseDtos = new ArrayList<>();
        chatMessages.forEach(chatMessage -> {
            chatMessageResponseDtos.add(new ChatMessageResponseDto(
                    chatMessage.getChatMessageId(),
                    chatMessage.getSender().getProfileId(),
                    chatMessage.getRecipient().getProfileId(),
                    chatMessage.getSender().getName(),
                    chatMessage.getRecipient().getName(),
                    chatMessage.getContent(),
                    chatMessage.getTimestamp()));
        });
        return ResponseEntity.ok().body(chatMessageResponseDtos);
    }

    @GetMapping("/api/chatRooms")
    public ResponseEntity<List<ChatRoomResponseDto>> getChatRooms(Principal principal) {
        Account account = accountService.findByUsername(principal.getName());
        Profile profile = account.getProfile();
        Optional<List<ChatRoom>> chatRooms = chatRoomService.getChatRoomsByProfile(profile);
       List<ChatRoomResponseDto> chatRoomsResponseDto = new ArrayList<>();
        chatRooms.get().forEach(chatRoom -> {
            chatRoomsResponseDto.add(new ChatRoomResponseDto(chatRoom.getChatId(), chatRoom.getRecipient().getName(), chatRoom.getRecipient().getProfilePictureUrl(), chatRoom.getSender().getProfileId(), chatRoom.getRecipient().getProfileId()));
        });
        if (chatRooms.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok().body(chatRoomsResponseDto);
    }

    @GetMapping("/createChatRoom/{recipientId}")
    public ResponseEntity<ChatRoomResponseDto> createChatRoom(Principal principal, @PathVariable Long recipientId) {
        Account account = accountService.findByUsername(principal.getName());
        Profile profile = account.getProfile();
        Optional<String> chatRoomId = chatRoomService.getChatRoomId(profile.getProfileId(), recipientId, true);
       List<ChatRoom> chatRooms = chatRoomService.getChatRoomsByProfile(profile).get();

       //Find ChatRoom in Chatroooms with chatRoomId
       ChatRoom chatRoom = chatRooms.stream().filter(chatRoom1 -> chatRoom1.getChatId().equals(chatRoomId.get())).findFirst().orElse(null);
       ChatRoomResponseDto chatRoomResponseDto = new ChatRoomResponseDto(chatRoom.getChatId(), chatRoom.getRecipient().getName(), chatRoom.getRecipient().getProfilePictureUrl(), chatRoom.getSender().getProfileId(), chatRoom.getRecipient().getProfileId());
        return new ResponseEntity<>(chatRoomResponseDto, HttpStatus.OK);

    }
}
