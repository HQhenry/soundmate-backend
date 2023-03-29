package com.edu.basaoc.service;

import com.edu.basaoc.model.MatchResponseDto;
import com.edu.basaoc.model.entity.Account;
import com.edu.basaoc.model.entity.Match;
import com.edu.basaoc.model.entity.Profile;
import com.edu.basaoc.model.repository.MatchRepository;
import com.edu.basaoc.model.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final ProfileRepository profileRepository;

    public MatchService(MatchRepository matchRepository, ProfileRepository profileRepository) {
        this.matchRepository = matchRepository;
        this.profileRepository = profileRepository;
    }

    public void calculateMatches(Profile profile) {

        //Get all users except yourself
        List<Profile> profiles = profileRepository.findAll();
        profiles.remove(profile);

        double[] myProfileFeatures = new double[3];
        myProfileFeatures[0] = profile.getMainstreamFactor();
        myProfileFeatures[1] = profile.getNovelFactor();
        myProfileFeatures[2] = profile.getDiverseFactor();

        //Handling if profile is empty
        if (profiles.isEmpty()) {
            return;
        }

        //Calculate distance between my profile and all other profiles
        ProfileDistance[] profileDistanceObjects = new ProfileDistance[profiles.size()];

        for (int i = 0; i < profiles.size(); i++) {
            double[] profileFeatures = new double[3];
            profileFeatures[0] = profiles.get(i).getMainstreamFactor();
            profileFeatures[1] = profiles.get(i).getNovelFactor();
            profileFeatures[2] = profiles.get(i).getDiverseFactor();
            profileDistanceObjects[i] = new ProfileDistance(profiles.get(i), distance(myProfileFeatures, profileFeatures));
        }

        // Calculate how many neighbors to find
        int k = 0;
        int size = profiles.size();

        if (size < 6) {
            k = size;
        } else {
            k = 6;
        }

        Arrays.stream(profileDistanceObjects)
                .sorted(Comparator.comparingDouble(ProfileDistance::getDistance))
                .limit(k)
                .forEach((ProfileDistance neighborDistance) -> {
                    Profile neighbor = neighborDistance.getProfile();
                    List<Match> previousMatches = matchRepository.findAllByProfile1AndProfile2(profile, neighbor);
                    previousMatches.addAll(matchRepository.findAllByProfile1AndProfile2(neighbor, profile));
                    if (previousMatches.isEmpty()) {
                        Match match = new Match();
                        match.setProfile1(profile);
                        match.setProfile2(neighbor);
                        match.setMatchDate(LocalDate.now());
                        match.setDistance(neighborDistance.getDistance());
                        match.setMatchedOnType("Profile 1 mnd values: " + profile.getMainstreamFactor() + " " + profile.getNovelFactor() + " " + profile.getDiverseFactor() + " Profile 2 mnd values: " + neighbor.getMainstreamFactor() + " " + neighbor.getNovelFactor() + " " + neighbor.getDiverseFactor());
                        matchRepository.save(match);
                    }
                });
    }

    public List<MatchResponseDto> getMatches(Account account) {
        List<MatchResponseDto> matches = new ArrayList<>();

        Optional<List<Match>> matchOptional1 = matchRepository.findAllByProfile1(account.getProfile());
        Optional<List<Match>> matchOptional2 = matchRepository.findAllByProfile2(account.getProfile());

        if (matchOptional1.isPresent()) {
            for (Match match : matchOptional1.get()) {
                matches.add(new MatchResponseDto(
                        match.getProfile2().getProfileId(),
                        match.getMatchDate(),
                        match.getMatchedOnType(),
                        match.getDistance(),
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
                        match.getDistance(),
                        match.getProfile1().getName(),
                        match.getProfile1().getProfilePictureUrl(),
                        match.getProfile1().getAge()));
            }
        }
        matches.sort(Comparator.comparingDouble(MatchResponseDto::getDistance));
        return matches;
    }

    public static double distance(double[] profileFactors, double[] otherProfileFactors) {
        double sum = 0.0;
        for (int i = 0; i < profileFactors.length; i++) {
            sum += Math.pow(profileFactors[i] - otherProfileFactors[i], 2);
        }
        return Math.sqrt(sum);
    }

    public void recalcAllMatches() {
        List<Match> allMatches = matchRepository.findAll();
        for (Match match : allMatches) {
            double[] profile1Features = new double[3];
            profile1Features[0] = match.getProfile1().getMainstreamFactor();
            profile1Features[1] = match.getProfile1().getNovelFactor();
            profile1Features[2] = match.getProfile1().getDiverseFactor();

            double[] profile2Features = new double[3];
            profile2Features[0] = match.getProfile2().getMainstreamFactor();
            profile2Features[1] = match.getProfile2().getNovelFactor();
            profile2Features[2] = match.getProfile2().getDiverseFactor();

            match.setDistance(distance(profile1Features, profile2Features));
            matchRepository.save(match);
        }
        List<Profile> allProfiles = profileRepository.findAll();
        for (Profile profile : allProfiles) {
            calculateMatches(profile);
        }
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
