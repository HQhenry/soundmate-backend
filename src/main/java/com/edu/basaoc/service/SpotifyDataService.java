package com.edu.basaoc.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.User;

import java.io.IOException;

@Service
@Slf4j
public class SpotifyDataService {

    public SpotifyApi setCurrentCredentials(String accessToken) {
        return SpotifyApi.builder().setAccessToken(accessToken).build();
    }

    public String fetchUserId(String accessToken) {
        SpotifyApi spotifyApi = setCurrentCredentials(accessToken);
        try {
            final User user = spotifyApi.getCurrentUsersProfile().build().execute();
            return user.getId();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            log.error("Unexpected response from spotify", e);
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected response from spotify");
        }


    }
}
