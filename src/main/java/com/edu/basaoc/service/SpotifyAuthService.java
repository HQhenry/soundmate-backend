package com.edu.basaoc.service;

import com.edu.basaoc.model.payload.response.SpotifyAuthTokenResponse;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;

import java.io.IOException;
import java.util.Base64;

@Service
@Slf4j
public class SpotifyAuthService {

    @Value("${soundmate.spotifyClientId}")
    private String clientId = "";
    @Value("${soundmate.spotifyClientSecret}")
    private String clientSecret = "";

    private String tokenUri = "https://accounts.spotify.com/api/token";

    private final SpotifyApi spotifyApi;

    public SpotifyAuthService() {
        this.spotifyApi = SpotifyApi.builder().build();
    }

    public AuthorizationCodeCredentials getFirstAccess(String authToken, String redirectUri) {
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "grant_type=authorization_code&code=" + authToken + "&redirect_uri=" + redirectUri);
            String auth = clientId + ":" + clientSecret;
            Request request = new Request.Builder()
                    .url("https://accounts.spotify.com/api/token")
                    .method("POST", body)
                    .addHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString(auth.getBytes()))
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() == HttpStatus.SC_OK) {
                SpotifyAuthTokenResponse credentials = new Gson().fromJson(response.body().string(), SpotifyAuthTokenResponse.class);

                return new AuthorizationCodeCredentials.Builder()
                        .setAccessToken(credentials.getAccess_token())
                        .setRefreshToken(credentials.getRefresh_token())
                        .setScope(credentials.getScope())
                        .setExpiresIn(credentials.getExpires_in())
                        .setTokenType(credentials.getToken_type())
                        .build();
            } else {
                throw new AuthenticationServiceException("Unexpected response from spotify");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public AuthorizationCodeCredentials refreshAccessToken(String refreshToken) {
        try {
            return spotifyApi.authorizationCodeRefresh(clientId, clientSecret, refreshToken).build().execute();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            log.info("Could not refresh spotify access", e);
            throw new AuthenticationServiceException("Could not refresh spotify access", e);
        }
    }

}
