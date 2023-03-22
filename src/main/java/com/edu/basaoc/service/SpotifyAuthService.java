package com.edu.basaoc.service;

import com.edu.basaoc.model.entity.Account;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
public class SpotifyAuthService {

    @Value("${soundmate.spotifyClientId}")
    private String clientId = "";
    @Value("${soundmate.spotifyClientSecret}")
    private String clientSecret = "";

    private final SpotifyApi spotifyApi;

    public SpotifyAuthService() {
        this.spotifyApi = SpotifyApi.builder().build();
    }

    public AuthorizationCodeCredentials getFirstAccess(String authToken, URI redirectUri) {
        try {
            return spotifyApi.authorizationCode(clientId, clientSecret, authToken, redirectUri).build().execute();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            log.info("Unexpected response from spotify", e);
            throw new AuthenticationServiceException("Unexpected response from spotify", e);
        }
    }

    public AuthorizationCodeCredentials refreshAccessToken(String refreshToken){
        try {
            return spotifyApi.authorizationCodeRefresh(clientId, clientSecret, refreshToken).build().execute();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            log.info("Could not refresh spotify access", e);
            throw new AuthenticationServiceException("Could not refresh spotify access", e);
        }
    }

}
