package com.edu.basaoc.controller;

import com.edu.basaoc.model.GenreDto;
import com.edu.basaoc.model.MatchResponseDto;
import com.edu.basaoc.model.entity.Account;
import com.edu.basaoc.model.entity.Genre;
import com.edu.basaoc.model.entity.Profile;
import com.edu.basaoc.model.mapper.GenreDtoMapper;
import com.edu.basaoc.service.AccountService;
import com.edu.basaoc.service.MatchService;
import com.edu.basaoc.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/match")
@CrossOrigin
public class MatchController {

    private final AccountService accountService;
    private final MatchService matchService;

    private final ProfileService profileService;



    public MatchController(AccountService accountService, MatchService matchService, ProfileService profileService) {
        this.accountService = accountService;
        this.matchService = matchService;
        this.profileService = profileService;

    }

    @GetMapping
    public ResponseEntity<List<MatchResponseDto>> getMatches(Principal principal) {
        Account account = accountService.findByUsername(principal.getName());
        List<MatchResponseDto> matches = matchService.getMatches(account);
        if (matches.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok().body(matches);
    }

    @GetMapping("/recalc")
    public ResponseEntity<String> recalcMatches() {
        matchService.recalcAllMatches();
        return new ResponseEntity<>("Finished recalculating", HttpStatus.OK);
    }
}
