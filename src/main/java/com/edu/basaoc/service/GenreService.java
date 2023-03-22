package com.edu.basaoc.service;

import com.edu.basaoc.model.GenreDto;
import com.edu.basaoc.model.entity.Account;
import com.edu.basaoc.model.entity.Genre;
import com.edu.basaoc.model.entity.Profile;
import com.edu.basaoc.model.repository.GenreRepository;
import com.edu.basaoc.model.repository.ProfileRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.edu.basaoc.service.ProfileService.genreDtoMapper;
import static org.aspectj.weaver.Shadow.ExceptionHandler;

@Service public class GenreService {



    private final GenreRepository genreRepository;
    private final ProfileRepository profileRepository;
    private final SpotifyDataService spotifyDataService;
    private final AccountService accountService;


    public GenreService(GenreRepository genreRepository, ProfileRepository profileRepository, SpotifyDataService spotifyDataService, AccountService accountService) {
        this.genreRepository = genreRepository;
        this.profileRepository = profileRepository;
        this.spotifyDataService = spotifyDataService;
        this.accountService = accountService;
    }

    public List<GenreDto> setTopGenres(Account account) throws RuntimeException{

        //TODO: remove top genres of user

        List<String> genres = spotifyDataService.getUsersTopGenres(account, accountService);
       if (genres == null || genres.size() == 0) {
            throw new RuntimeException("No genres found");
        }
        List<GenreCounter> genreCounters = new ArrayList<>();

        HashMap<String, Integer> genreOccurences = new HashMap<String, Integer>();
        for (String genre : genres) {
            if (genreOccurences.containsKey(genre)) {
                genreOccurences.put(genre, genreOccurences.get(genre) + 1);
            } else {
                genreOccurences.put(genre, 1);
            }
        }
        for (Map.Entry<String, Integer> entry : genreOccurences.entrySet()) {
            genreCounters.add(new GenreCounter(entry.getKey(), entry.getValue()));
        }
        genreCounters.sort(Comparator.comparingInt(GenreCounter::getCounter).reversed());
        List<GenreDto> firstNElementsList = genreCounters.stream().limit(10).collect(Collectors.toList())
                .stream().map(genreCounter -> new GenreDto(genreCounter.genre)).collect(Collectors.toList());

        for (GenreDto genreDto : firstNElementsList) {
            Optional<Genre> genre = genreRepository.findByName(genreDto.getName());
            if (genre.isEmpty()) {
                Genre newGenre = new Genre();
                newGenre.setName(genreDto.getName());
                newGenre.addProfile(profileRepository.findByAccount(account));
                genreRepository.save(newGenre);
            } else {
                genre.get().addProfile(profileRepository.findByAccount(account));
                genreRepository.save(genre.get());
            }
        }
        return firstNElementsList;
    }

    public Set<GenreDto> getTopGenres(Account account) {
        Profile profile = profileRepository.findByAccount(account);
        return profile.getTopGenres().stream().map(genreDtoMapper::entityToDto).collect(Collectors.toSet());
    }
    @AllArgsConstructor
    @Getter
    class GenreCounter{
        String genre;
        int counter;
    }
}
