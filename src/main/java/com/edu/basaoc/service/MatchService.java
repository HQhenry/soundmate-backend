package com.edu.basaoc.service;

import com.edu.basaoc.model.MatchResponseDto;
import com.edu.basaoc.model.entity.Account;
import com.edu.basaoc.model.entity.Match;
import com.edu.basaoc.model.repository.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MatchService {

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public List<MatchResponseDto> getMatches(Account account) {
        List<MatchResponseDto> matches = new ArrayList<>();

        Optional<List<Match>> matchOptional1 =  matchRepository.findAllByProfile1(account.getProfile());
        Optional<List<Match>> matchOptional2 = matchRepository.findAllByProfile2(account.getProfile());

        if (matchOptional1.isPresent()) {
            for (Match match : matchOptional1.get()) {
                matches.add(new MatchResponseDto(
                        match.getProfile2().getProfileId(),
                        match.getMatchDate(),
                        match.getMatchedOnType(),
                        match.getProfile2().getName(),
                        "https://i.scdn.co/image/ab6775700000ee850f1daca7ddd0bc34a7d8ec2c"));
            }
        }

        if (matchOptional2.isPresent()) {
            for (Match match : matchOptional2.get()) {
                matches.add(new MatchResponseDto(
                        match.getProfile1().getProfileId(),
                        match.getMatchDate(),
                        match.getMatchedOnType(),
                        match.getProfile1().getName(),
                        "https://i.scdn.co/image/ab6775700000ee850f1daca7ddd0bc34a7d8ec2c")); //TODO:
            }
        }
        return matches;
    }
}
