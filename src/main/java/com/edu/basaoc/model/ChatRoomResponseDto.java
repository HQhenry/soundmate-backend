package com.edu.basaoc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ChatRoomResponseDto {
    private String chatId;
    private String name;
    private String profilePictureUrl;

    private Long senderProfileId;
    private Long recipientProfileId;
}
