package com.edu.basaoc.controller;

import com.edu.basaoc.model.entity.Account;
import com.edu.basaoc.model.ArtistDto;
import com.edu.basaoc.model.entity.Profile;
import com.edu.basaoc.service.AccountService;
import com.edu.basaoc.service.ArtistService;
import com.edu.basaoc.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;

import java.io.IOException;
import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin
public class ProfileController {

    private final AccountService accountService;
    private final ProfileService profileService;

    //For testing
    private static final String accessToken = "BQBCoi4SBBzjo8CIoXKQqwxE-Bv2_X0BkUFkfSQeDNiwxSacu3u9VaCWVHOzHR5lbYctautTl8TBgLEVS6lUbl8nisEAIWgHf50EKHY8V1qt_JH8qkjlT-zVuXvWvX6w-itcEvDza0shjt1OsGCZnwd3rgX6LioWragM5i9LXEfm_Z31z2JtjXBj7j0YvvswRgKOJhxqOE1A";


    public ProfileController(AccountService accountService, ProfileService profileService) {
        this.accountService = accountService;
        this.profileService = profileService;
    }

    private SpotifyApi getSpotifyapi(String accessToken, Principal principal) {
        String username = principal.getName();
        //For testing
        Account account = accountService.getAccountByAccountId(1L);
        //Account account = accountService.findByUsername(username);
        if (account.getAccessToken() != null) {
            accessToken = account.getAccessToken();
        }
        return new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();
    }

    @GetMapping(value = "/fetch-top-artists")
    //TODO: don't forget to change the profile in Configurations
    //TODO: has to be implemented in scheduler later
    public ArtistDto[] fetchTopArtists(Principal principal) {
        SpotifyApi spotifyApi = getSpotifyapi(accessToken, principal);
        final GetUsersTopArtistsRequest getUsersTopArtistsRequest = spotifyApi.getUsersTopArtists()
                .limit(10)
                .build();
        try {
            final Paging<Artist> artistPaging = getUsersTopArtistsRequest.execute();
            ArtistDto[] artistResponses = new ArtistDto[artistPaging.getItems().length];
            Artist[] artists = artistPaging.getItems();
            for (int i = 0; i < artists.length; i++) {
                artistResponses[i] = new ArtistDto(artists[i].getName(), artists[i].getImages()[0].getUrl());
                //profileService.setTopArtist(artistResponses[i], accountService.findByUsername(principal.getName()));
                profileService.setTopArtist(artistResponses[i], accountService.getAccountByAccountId(1L));

            }
            return artistResponses;
        } catch (IOException | SpotifyWebApiException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (org.apache.hc.core5.http.ParseException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @GetMapping(value = "/get-top-artists")
    public ResponseEntity<Set<ArtistDto>> getTopArtists(Principal principal) {
        return ResponseEntity.ok().body( profileService.getTopArtists(accountService.findByUsername(principal.getName())));
    }

}
