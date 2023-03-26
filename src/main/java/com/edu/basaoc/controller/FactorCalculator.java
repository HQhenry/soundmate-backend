package com.edu.basaoc.controller;

import com.edu.basaoc.model.entity.Account;
import com.edu.basaoc.service.SpotifyDataService;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class FactorCalculator {

    private static SpotifyDataService dataService;
    private static Account account;

    public FactorCalculator(SpotifyDataService dataService, Account account) {
        this.dataService = dataService;
        this.account = account;
    }

    static double calculateMainstreamFactor() throws Exception {

        Artist[] topArtists = dataService.getUsersTopArtists(account, 50);

        double totalPopularity = Arrays.stream(topArtists).mapToDouble(Artist::getPopularity).sum();//stream().mapToDouble(Artist::getPopularity).sum();
        return totalPopularity / topArtists.length / 100;



        //TEST
        /*
        int[] testValues = {}
        for (Integer count : genreCountMap.values()) {
            double probability = (double) count / totalGenres;
            shannonEntropy -= probability * (Math.log(probability) / Math.log(2));
        }
        System.out.println("totalgenres: " + totalGenres);
        double maxEntropy = Math.log(totalGenres) / Math.log(2);
        double normalizedDiversityFactor = shannonEntropy / maxEntropy;*/

    }


    static double calculateDiversityFactor() throws Exception {
        Artist[] topArtists = dataService.getUsersTopArtists(account, 50);

        Map<String, Integer> genreCountMap = new HashMap<>();
        int totalGenres = 0;
        for (Artist artist : topArtists) {
            Set<String> uniqueGenres = new HashSet<>(Arrays.asList(artist.getGenres()));
            for (String genre : uniqueGenres) {
                genreCountMap.put(genre, genreCountMap.getOrDefault(genre, 0) + 1);
                totalGenres++;
            }
        }

        double shannonEntropy = 0.0;
        genreCountMap.keySet().forEach(key -> System.out.println(key + " " + key + ", occurances:  " + genreCountMap.get(key)));
        for (Integer count : genreCountMap.values()) {
            double probability = (double) count / totalGenres;
            shannonEntropy -= probability * (Math.log(probability) / Math.log(2));
        }
        System.out.println("totalgenres: " + totalGenres);
        double maxEntropy = Math.log(totalGenres) / Math.log(2);
        double normalizedDiversityFactor = shannonEntropy / maxEntropy;

        return normalizedDiversityFactor;
    }
    static double calculateNoveltyFactor() throws Exception {
        Track[] topTracks = dataService.getUsersTopTracks(account, 50);

        double totalDays = 0;
        int numTracks = topTracks.length;
        LocalDate currentDate = LocalDate.now();

        long maxDaysSinceRelease = 0;
        int amountNewSongs = 0;
        for (Track track : topTracks) {
            LocalDate releaseDate = LocalDate.parse(track.getAlbum().getReleaseDate());
            long daysSinceRelease = ChronoUnit.DAYS.between(releaseDate, currentDate);
            totalDays += daysSinceRelease;
            if (daysSinceRelease < 365) {
                amountNewSongs++;
            }
            if (daysSinceRelease > maxDaysSinceRelease) {
                maxDaysSinceRelease = daysSinceRelease;
                //System.out.println("days since release: " + daysSinceRelease);

            }
        }



        double averageDaysSinceRelease = totalDays / numTracks;
        double maxDays = 365 ; //6 * 30 ;// 6 Monate // 365 *5
        //System.out.println("maxDays: " + maxDays);
        double noveltyFactor = averageDaysSinceRelease / maxDays;
        //double noveltyFactor = amountNewSongs / numTracks;
        //System.out.println("amountNew Songs: " + amountNewSongs);
        //System.out.println("numTracks: " + numTracks);

        // Normieren
        return amountNewSongs / (double) numTracks;
    }
}
