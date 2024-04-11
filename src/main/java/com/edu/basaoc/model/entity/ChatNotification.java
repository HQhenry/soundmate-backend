package com.edu.basaoc.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ChatNotification {

    private String chatMessageId;
    private String chatId;
    private String senderId;
    private String recipientId;
    private String content;

    private Timestamp timestamp;
}
