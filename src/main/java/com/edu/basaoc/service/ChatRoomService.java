package com.edu.basaoc.service;

import com.edu.basaoc.model.entity.ChatRoom;
import com.edu.basaoc.model.entity.Profile;
import com.edu.basaoc.model.repository.ChatRoomRepository;
import com.edu.basaoc.model.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatRoomService {

    private final ChatRoomRepository repository;
    private final ProfileRepository profileRepository;
    public ChatRoomService(ChatRoomRepository repository, ProfileRepository profileRepository) {
        this.repository = repository;
        this.profileRepository = profileRepository;
    }

    public Optional<String> getChatRoomId( long senderId,
    long recipientId, boolean createNewRoomIfNotExists) {
        return repository.findBySenderProfileIdAndRecipientProfileId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if (!createNewRoomIfNotExists) {
                        return Optional.empty();
                    }
                    String chatId = createChatRoom(senderId, recipientId);
                    return Optional.of(chatId);
                });
    }

    public String createChatRoom(long senderId, long recipientId) {
        String chatId = String.format("%s_%s", senderId, recipientId);

        Profile sender = profileRepository.findById(senderId).get();
        Profile recipient = profileRepository.findById(recipientId).get();

        ChatRoom senderRecipientChatRoom = new ChatRoom();
        senderRecipientChatRoom.setChatId(chatId);
        senderRecipientChatRoom.setSender(sender);
        senderRecipientChatRoom.setRecipient(recipient);

        ChatRoom recipientSenderChatRoom = new ChatRoom();
        recipientSenderChatRoom.setChatId(chatId);
        recipientSenderChatRoom.setSender(recipient);
        recipientSenderChatRoom.setRecipient(sender);

        repository.save(senderRecipientChatRoom);
        repository.save(recipientSenderChatRoom);

        return chatId;
    }

    public Optional<List<ChatRoom>> getChatRoomsByProfile(Profile sender) {
        return repository.findChatRoomsBySender(sender);
    }
}
