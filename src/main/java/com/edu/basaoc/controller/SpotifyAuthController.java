package com.edu.basaoc.controller;

import com.edu.basaoc.model.entity.Account;
import com.edu.basaoc.model.payload.response.JwtResponse;
import com.edu.basaoc.payload.request.SpotifyLoginRequest;
import com.edu.basaoc.security.jwt.JwtUtils;
import com.edu.basaoc.service.AccountService;
import com.edu.basaoc.service.SpotifyAuthService;
import com.edu.basaoc.service.SpotifyDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/api/auth/spotify")
@CrossOrigin
public class SpotifyAuthController {

    private final AuthenticationManager authenticationManager;
    private final SpotifyAuthService authService;
    private final AccountService accountService;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;


    public SpotifyAuthController(
            AuthenticationManager authenticationManager,
            SpotifyAuthService authService,
            AccountService accountService,
            PasswordEncoder encoder,
            JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.accountService = accountService;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping
    public ResponseEntity<JwtResponse> signIn(@RequestBody SpotifyLoginRequest loginRequest) {
        AuthorizationCodeCredentials credentials = authService.getAccessToken(
                loginRequest.getAuthCode(),
                loginRequest.getRedirectUri()
        );
        String userId = new SpotifyDataService().fetchUserId(credentials.getAccessToken());
        if (!accountService.userExists(userId)) {
            Account account = new Account(userId, encoder.encode(credentials.toString()));
            account.setAccessToken(credentials.getAccessToken());
            account.setRefreshToken(credentials.getRefreshToken());
            account.setAccessExpiresOn(Instant.now().plus(credentials.getExpiresIn(), ChronoUnit.SECONDS));
            accountService.createUser(account);
        } else {
            Account account = accountService.findByUsername(userId);
            account.setAccessToken(credentials.getAccessToken());
            account.setRefreshToken(credentials.getRefreshToken());
            account.setAccessExpiresOn(Instant.now().plus(credentials.getExpiresIn(), ChronoUnit.SECONDS));
            account.setPassword(encoder.encode(credentials.toString()));
            accountService.updateUser(account);
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userId, credentials.toString()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }
}
