package com.edu.basaoc.service;

import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

import java.io.IOException;

public class SpotifyDataService {

    @Value("${soundmate.spotifyClientId}")
    private static String clientId;
    @Value("${soundmate.spotifyClientSecret}")
    private static String clientSecret;

    private SpotifyApi spotifyApi(String accessToken, String refreshToken){
        return new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken)
                .build();
    }

    public String fetchUserId(String accessToken, String refreshToken) {
        try {
            SpotifyApi api = spotifyApi(accessToken, refreshToken);
            final GetCurrentUsersProfileRequest request = api.getCurrentUsersProfile().build();
            return request.execute().getId();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SpotifyWebApiException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
