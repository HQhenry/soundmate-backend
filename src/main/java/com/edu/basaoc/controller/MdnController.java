package com.edu.basaoc.controller;

import com.edu.basaoc.model.MatchResponseDto;
import com.edu.basaoc.model.entity.Account;
import com.edu.basaoc.service.AccountService;
import com.edu.basaoc.service.SpotifyAuthService;
import com.edu.basaoc.service.SpotifyDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/mdn")
@CrossOrigin

public class MdnController {
    @Value("${soundmate.spotifyClientId}")
    private String clientId = "";

    private final SpotifyAuthService spotifyAuthService;
    private final PasswordEncoder encoder;
    private final AccountService accountService;
    private final SpotifyDataService dataService;


    public MdnController(SpotifyAuthService spotifyAuthService, PasswordEncoder encoder, AccountService accountService, SpotifyDataService dataService) {
        this.spotifyAuthService = spotifyAuthService;
        this.encoder = encoder;
        this.accountService = accountService;
        this.dataService = dataService;
    }

    @GetMapping
    public ResponseEntity<double[]> getMdnFactors(Principal principal) {
        Account account = accountService.findByUsername(principal.getName());

        FactorCalculator factorCalculator = new FactorCalculator(dataService, account);
        double [] mdnFactors = new double[3];
        double noveltyFactor = 0;
        double mainstreamFactor = 0;
        double diversityFactor = 0;
        try {
            mdnFactors[2] = factorCalculator.calculateNoveltyFactor(); //noveltyFactor
            mdnFactors[0] = factorCalculator.calculateMainstreamFactor(); // mainstreamFactor
            mdnFactors[1] = factorCalculator.calculateDiversityFactor(); // diversityFactor
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body(mdnFactors);
    }
}
