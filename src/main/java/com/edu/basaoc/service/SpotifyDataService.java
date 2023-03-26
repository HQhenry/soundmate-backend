package com.edu.basaoc.service;

import com.edu.basaoc.model.entity.Account;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Transient;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.SavedTrack;
import se.michaelthelin.spotify.model_objects.specification.User;
import se.michaelthelin.spotify.requests.data.artists.GetSeveralArtistsRequest;
import se.michaelthelin.spotify.requests.data.library.GetUsersSavedTracksRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Service
@Slf4j
public class SpotifyDataService {

    @Value("${soundmate.spotifyClientId}")
    private String clientId = "";

    private final SpotifyAuthService spotifyAuthService;
    private final PasswordEncoder encoder;
    private final AccountService accountService;

    public SpotifyDataService(SpotifyAuthService spotifyAuthService, PasswordEncoder encoder, AccountService accountService) {
        this.spotifyAuthService = spotifyAuthService;
        this.encoder = encoder;
        this.accountService = accountService;
    }

    public String fetchUserId(String accessToken) {
        try {
            final User user = SpotifyApi
                    .builder()
                    .setClientId(clientId)
                    .setAccessToken(accessToken)
                    .build()
                    .getCurrentUsersProfile()
                    .build()
                    .execute();
            return user.getId();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            log.error("Unexpected response from spotify", e);
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected response from spotify");
        }
    }

    public Artist[] getUsersTopArtists(Account account) {
        try {
            SpotifyApi spotifyApi = getSpotifyApi(account);
            final GetUsersTopArtistsRequest getUsersTopArtistsRequest = spotifyApi.getUsersTopArtists()
                    .limit(10).build();
            return getUsersTopArtistsRequest.execute().getItems();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            log.error("Error: " + e.getMessage());
        }
        return new Artist[]{};
    }

    public List<String> getUsersTopGenres(Account account) {
        try {
            SpotifyApi spotifyApi = getSpotifyApi(account);
            List<String> genres = new ArrayList<>();
            final GetUsersSavedTracksRequest getUsersSavedTracksRequest = spotifyApi.getUsersSavedTracks().build();
            SavedTrack[] savedTracks = getUsersSavedTracksRequest.execute().getItems();
            if (savedTracks.length != 0) {
                String[] artistsIds = Arrays.stream(savedTracks).map(track -> track.getTrack().getArtists()[0].getId()).toArray(String[]::new);
                final GetSeveralArtistsRequest getSeveralArtistsRequest = spotifyApi.getSeveralArtists(artistsIds)
                        .build();
                Artist[] artists = getSeveralArtistsRequest.execute();

                for (Artist artist : artists) {
                    genres.addAll(Arrays.asList(artist.getGenres()));
                }
            }
            return genres;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            log.error("Error: " + e.getMessage());
        }
        return Collections.emptyList();
    }

    private SpotifyApi getSpotifyApi(Account account) {
        SpotifyApi.Builder userApi = SpotifyApi.builder().setClientId(clientId);
        if (account.getAccessExpiresOn().isBefore(Instant.now())) {
            AuthorizationCodeCredentials credentials = spotifyAuthService.refreshAccessToken(account.getRefreshToken());
            account.setAccessToken(credentials.getAccessToken());
            account.setRefreshToken(credentials.getRefreshToken());
            account.setAccessExpiresOn(Instant.now().plus(credentials.getExpiresIn(), ChronoUnit.SECONDS));
            account.setPassword(encoder.encode(credentials.toString()));
            accountService.updateUser(account);
        }
        return userApi.setAccessToken(account.getAccessToken()).build();
    }

    public String fetchUserProfilePicture(Account account) {
        SpotifyApi spotifyApi = getSpotifyApi(account);

        try {
            final User user = spotifyApi.getCurrentUsersProfile().build().execute();
            if (user.getImages().length == 0) {
                return null;
            }
            return user.getImages()[0].getUrl();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            log.error("Unexpected response from spotify", e);
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected response from spotify");
        }
    }
}
