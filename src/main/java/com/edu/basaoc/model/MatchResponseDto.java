package com.edu.basaoc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class MatchResponseDto {
    private long profileId;
    LocalDate matchDate;
    ListeningType listeningType;
    String name;
    String profilePictureUrl;


}
