package com.edu.basaoc.model;

import lombok.*;

import java.time.Instant;


@Data
public class AccountDto {

    private Long id;
    private String spotifyUserId;
    private String accessToken;
    private String refreshToken;
    private Instant accessExpiresOn;

}
