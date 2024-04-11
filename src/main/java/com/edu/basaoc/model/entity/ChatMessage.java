package com.edu.basaoc.model.entity;

import com.edu.basaoc.model.repository.ProfileRepository;
import com.edu.basaoc.service.ProfileService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "chat_message")

public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Long chatMessageId;

    @Setter
    @Column(name = "chatId")
    private String chatId;

    @Setter
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "sender_fk", referencedColumnName = "profile_id")
    private Profile sender;

    @Setter
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "recipient_fk", referencedColumnName = "profile_id")
    private Profile recipient;

    @Setter
    @Column(name = "content")
    private String content;

    @Setter
    @Column(name = "timestamp")
    private Timestamp timestamp;

}
