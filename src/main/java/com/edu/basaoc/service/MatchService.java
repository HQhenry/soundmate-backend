package com.edu.basaoc.service;

import com.edu.basaoc.model.MatchResponseDto;
import com.edu.basaoc.model.entity.Account;
import com.edu.basaoc.model.entity.Match;
import com.edu.basaoc.model.entity.Profile;
import com.edu.basaoc.model.repository.MatchRepository;
import com.edu.basaoc.model.repository.ProfileRepository;
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
                        match.getProfile2().getProfilePictureUrl(),
                        match.getProfile2().getAge()));
            }
        }

        if (matchOptional2.isPresent()) {
            for (Match match : matchOptional2.get()) {
                matches.add(new MatchResponseDto(
                        match.getProfile1().getProfileId(),
                        match.getMatchDate(),
                        match.getMatchedOnType(),
                        match.getProfile1().getName(),
                        match.getProfile1().getProfilePictureUrl(),
                        match.getProfile1().getAge()));
            }
        }
        return matches;
    }

    public static double distance(double[] profileFactors, double[] otherProfileFactors) {
        double sum = 0.0;
        for (int i = 0; i < profileFactors.length; i++) {
            sum += Math.pow(profileFactors[i] - otherProfileFactors[i], 2);
        }
        return Math.sqrt(sum);
    }
}

class ProfileDistance implements Comparable<ProfileDistance> {
    private Profile profile;
    private double distance;

    public ProfileDistance(Profile profile, double distance) {
        this.profile = profile;
        this.distance = distance;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(ProfileDistance o) {
        return Double.compare(this.distance, o.distance);
    }
}