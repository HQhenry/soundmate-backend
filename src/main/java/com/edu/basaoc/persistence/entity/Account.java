package com.edu.basaoc.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "account", schema = "soundmate_accounts")
public class Account {

    @Id
    @Column(name = "account_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "spotify_user_id")
    private String spotifyUserId;

    @Column(name = "access_token")
    @Setter
    private String accessToken;

    @Column(name = "refresh_token")
    @Setter
    private String refreshToken;

    @Column(name = "access_expires_on")
    @Setter
    private Instant accessExpiresOn;

    public Account(String spotifyUserId, String accessToken, String refreshToken, Instant accessExpiresOn) {
        this.spotifyUserId = spotifyUserId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessExpiresOn = accessExpiresOn;
    }
}
