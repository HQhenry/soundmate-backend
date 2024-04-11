package com.edu.basaoc.model.repository;

import com.edu.basaoc.model.entity.ChatRoom;
import com.edu.basaoc.model.entity.Genre;
import com.edu.basaoc.model.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom>findBySenderProfileIdAndRecipientProfileId(long senderId, long recipientId);

    Optional<List<ChatRoom>>findChatRoomsBySender(Profile profile);
}
