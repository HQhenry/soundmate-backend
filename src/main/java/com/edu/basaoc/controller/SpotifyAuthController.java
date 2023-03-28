package com.edu.basaoc.controller;

import com.edu.basaoc.model.entity.Account;
import com.edu.basaoc.model.entity.Profile;
import com.edu.basaoc.model.payload.response.JwtResponse;
import com.edu.basaoc.payload.request.SpotifyLoginRequest;
import com.edu.basaoc.security.jwt.JwtUtils;
import com.edu.basaoc.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/api/auth/spotify")
@CrossOrigin
public class SpotifyAuthController {

    private final AuthenticationManager authenticationManager;
    private final SpotifyAuthService authService;
    private final SpotifyDataService dataService;
    private final AccountService accountService;
    private final ProfileService profileService;
    private final PasswordEncoder encoder;
    private final MatchService matchService;
    private final JwtUtils jwtUtils;


    public SpotifyAuthController(
            AuthenticationManager authenticationManager,
            SpotifyAuthService authService,
            SpotifyDataService dataService, AccountService accountService,
            ProfileService profileService,
            MatchService matchService,
            PasswordEncoder encoder,
            JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.dataService = dataService;
        this.accountService = accountService;
        this.profileService = profileService;
        this.matchService = matchService;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping
    public ResponseEntity<JwtResponse> signIn(@RequestBody SpotifyLoginRequest loginRequest) {
        AuthorizationCodeCredentials credentials = authService.getFirstAccess(
                loginRequest.getAuthCode(),
                loginRequest.getRedirectUri()
        );
        String userId = dataService.fetchUserId(credentials.getAccessToken());

        if (!accountService.userExists(userId)) {
            Account account = new Account(userId, encoder.encode(credentials.toString()));
            account.setAccessToken(credentials.getAccessToken());
            account.setRefreshToken(credentials.getRefreshToken());
            account.setAccessExpiresOn(Instant.now().plus(credentials.getExpiresIn(), ChronoUnit.SECONDS));
            accountService.createUser(account);

            // TODO move this into a listener
            Artist[] usersTopArtists = dataService.getUsersTopArtists(account, 10);
            List<String> usersTopGenres = dataService.getUsersTopGenres(account);
            String profileImageUrl = dataService.fetchUserProfilePicture(account);

            FactorCalculator factorCalculator = new FactorCalculator(dataService, account);
            double [] mdnFactors = new double[3];
            try {
                mdnFactors[0] = factorCalculator.calculateMainstreamFactor(); // mainstreamFactor
                mdnFactors[1] = factorCalculator.calculateDiversityFactor(); // diversityFactor
                mdnFactors[2] = factorCalculator.calculateNoveltyFactor(); //noveltyFactor
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            profileService.createProfile(account, usersTopArtists, usersTopGenres, profileImageUrl, mdnFactors);
            Profile profile = profileService.getProfile(account);
            matchService.calculateMatches(profile);
        } else {
            Account account = accountService.findByUsername(userId);
            account.setAccessToken(credentials.getAccessToken());
            account.setRefreshToken(credentials.getRefreshToken());
            account.setAccessExpiresOn(Instant.now().plus(credentials.getExpiresIn(), ChronoUnit.SECONDS));
            account.setPassword(encoder.encode(credentials.toString()));
            accountService.updateUser(account);

            Artist[] usersTopArtists = dataService.getUsersTopArtists(account, 10);
            List<String> usersTopGenres = dataService.getUsersTopGenres(account);
            String profileImageUrl = dataService.fetchUserProfilePicture(account);
            profileService.updateProfileImageUrl(account.getProfile(), profileImageUrl);
            profileService.updateProfileTopValues(account.getProfile(), usersTopArtists, usersTopGenres);
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userId, credentials.toString()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }
}
