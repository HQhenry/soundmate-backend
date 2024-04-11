package com.edu.basaoc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequestDto {
        private Long sender;
        private Long recipient;
        private String content;
        private Timestamp timestamp;
    }


