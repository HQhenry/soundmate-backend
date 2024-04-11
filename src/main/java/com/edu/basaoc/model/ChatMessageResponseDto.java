package com.edu.basaoc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class ChatMessageResponseDto {
    private Long chatMessageId;
    private Long senderProfileId;
    private Long recipientProfileId;

    private String senderName;

    private String recipientName;
    private String content;
    private Timestamp timestamp;
}
