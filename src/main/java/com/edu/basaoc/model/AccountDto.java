package com.edu.basaoc.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;


@Setter
@Getter
@NoArgsConstructor
public class AccountDto {

    private Long id;
    private String spotifyUserId;
    private String accessToken;
    private String refreshToken;
    private Instant accessExpiresOn;

    public AccountDto(String accessToken, String refreshToken, Instant accessExpiresOn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessExpiresOn = accessExpiresOn;
    }
}
