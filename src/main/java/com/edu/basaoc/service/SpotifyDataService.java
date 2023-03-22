package com.edu.basaoc.service;

import com.edu.basaoc.model.entity.Account;
import com.edu.basaoc.model.entity.Genre;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
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
import java.util.List;


@Service
@Slf4j
public class SpotifyDataService {
    @Value("${soundmate.spotifyClientId}")
    private static String clientId = "";

    public SpotifyApi setCurrentCredentials(String accessToken) {
        return SpotifyApi.builder().setAccessToken(accessToken).build();
    }

    private static SpotifyApi getSpotifyapi(Account account, AccountService accountService) {
        if (account.getAccessExpiresOn().isBefore(Instant.now())) {
            SpotifyAuthService spotifyAuthService = new SpotifyAuthService();
            AuthorizationCodeCredentials credentials = spotifyAuthService.refreshAccessToken(account.getRefreshToken());
            account.setAccessToken(credentials.getAccessToken());
            account.setRefreshToken(credentials.getRefreshToken());
            account.setAccessExpiresOn(Instant.now().plus(credentials.getExpiresIn(), ChronoUnit.SECONDS));
            //account.setPassword(encoder.encode(credentials.toString()));
            accountService.updateUser(account);
        }
        //Hier neues refresh token holen und speichern
        return new SpotifyApi.Builder()
                .setAccessToken(account.getAccessToken())
                .setClientId(clientId)
                .setRefreshToken(account.getRefreshToken())
                .build();
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

    public  Artist[] getUsersTopArtists(Account account, AccountService accountService) {
        try {
            SpotifyApi spotifyApi = getSpotifyapi(account, accountService);
            final GetUsersTopArtistsRequest getUsersTopArtistsRequest = spotifyApi.getUsersTopArtists()
                    .limit(10).build();
            return getUsersTopArtistsRequest.execute().getItems();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public List<String> getUsersTopGenres(Account account, AccountService accountService) {
        try {
            SpotifyApi spotifyApi = getSpotifyapi(account, accountService);
            List<String> genres = new ArrayList<>();
            final GetUsersSavedTracksRequest getUsersSavedTracksRequest = spotifyApi.getUsersSavedTracks().build();
            SavedTrack[] savedTracks = getUsersSavedTracksRequest.execute().getItems();
            if (savedTracks.length != 0){
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
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
}
