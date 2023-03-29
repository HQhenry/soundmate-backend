package com.edu.basaoc.controller;

import com.edu.basaoc.model.MatchResponseDto;
import com.edu.basaoc.model.entity.Account;
import com.edu.basaoc.service.AccountService;
import com.edu.basaoc.service.MatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/match")
@CrossOrigin
public class MatchController {

    private final AccountService accountService;
    private final MatchService matchService;


    public MatchController(AccountService accountService, MatchService matchService) {
        this.accountService = accountService;
        this.matchService = matchService;
    }

    @GetMapping
    public ResponseEntity<List<MatchResponseDto>> getMatches(Principal principal) {
        Account account = accountService.findByUsername(principal.getName());
        List<MatchResponseDto> matches = matchService.getMatches(account);
        if (matches.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok().body(matchService.getMatches(account));
    }

    @GetMapping("/recalc")
    public ResponseEntity<String> recalcMatches() {
        matchService.recalcAllMatches();
        return new ResponseEntity<>("Finished recalculating", HttpStatus.OK);
    }
}
