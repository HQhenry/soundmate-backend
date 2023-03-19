package com.edu.basaoc.service;

import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;

import java.io.IOException;
import java.net.URI;

@Service
public class SpotifyAuthService {

    @Value("${soundmate.spotifyClientId}")
    private String clientId = "";
    @Value("${soundmate.spotifyClientSecret}")
    private String clientSecret = "";

    private final SpotifyApi spotifyApi;

    public SpotifyAuthService() {
        this.spotifyApi = SpotifyApi.builder().build();
    }

    public AuthorizationCodeCredentials getAccessToken(String authToken, URI redirectUri){
        try {
            return spotifyApi.authorizationCode(clientId, clientSecret, authToken, redirectUri).build().execute();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new AuthenticationServiceException("Unexpected response from spotify", e);
        }
    }

}
