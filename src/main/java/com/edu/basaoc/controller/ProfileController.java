package com.edu.basaoc.controller;

import com.edu.basaoc.model.GenreDto;
import com.edu.basaoc.model.ProfileResponseDto;
import com.edu.basaoc.model.entity.Account;
import com.edu.basaoc.model.ArtistDto;
import com.edu.basaoc.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin
public class ProfileController {

    private final AccountService accountService;
    private final ProfileService profileService;
    private final SpotifyDataService spotifyDataService;
    private final GenreService genreService;

     public ProfileController(AccountService accountService, ProfileService profileService, ArtistService artistService, GenreService genreService) {
        this.accountService = accountService;
        this.profileService = profileService;
         this.genreService = genreService;
         this.spotifyDataService = new SpotifyDataService();
    }


    @GetMapping(value = "/fetch-top-artists")
    // has to be implemented in scheduler later
    public ResponseEntity<ArtistDto[]> fetchTopArtists(Principal principal) {

        Artist[] artists = spotifyDataService.getUsersTopArtists(accountService.findByUsername(principal.getName()), accountService);
        if (artists == null || artists.length == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        ArtistDto[] artistResponses = new ArtistDto[artists.length];
        for (int i = 0; i < artists.length; i++) {
            artistResponses[i] = new ArtistDto(artists[i].getName(), artists[i].getImages()[0].getUrl());
            profileService.setTopArtist(artistResponses[i], accountService.findByUsername(principal.getName()));
        }
        return ResponseEntity.ok().body(artistResponses);
    }

    @GetMapping(value = "/get-top-artists")
    public ResponseEntity<Set<ArtistDto>> getTopArtists(Principal principal) {
        return ResponseEntity.ok().body( profileService.getTopArtists(accountService.findByUsername(principal.getName())));
    }

    @GetMapping(value = "/fetch-top-genres")
    // has to be implemented in scheduler later
    public ResponseEntity<List<GenreDto>> fetchTopGenres(Principal principal) {
        return ResponseEntity.ok().body(genreService.setTopGenres(accountService.findByUsername(principal.getName())));
    }

    @GetMapping
    public ResponseEntity<ProfileResponseDto> getProfile(Principal principal) {
        Account account = accountService.findByUsername(principal.getName());
        ProfileResponseDto profile = profileService.getProfile(account);
        return ResponseEntity.ok().body(profile);
    }
}
