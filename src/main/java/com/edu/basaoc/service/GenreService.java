package com.edu.basaoc.service;

import com.edu.basaoc.model.GenreDto;
import com.edu.basaoc.model.entity.Account;
import com.edu.basaoc.model.entity.Artist;
import com.edu.basaoc.model.entity.Genre;
import com.edu.basaoc.model.entity.Profile;
import com.edu.basaoc.model.mapper.GenreDtoMapper;
import com.edu.basaoc.model.repository.GenreRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GenreService {


    private static final GenreDtoMapper genreDtoMapper = Mappers.getMapper(GenreDtoMapper.class);
    private final GenreRepository genreRepository;


    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public void setTopGenres(Profile profile, Iterable<String> genres) throws RuntimeException {
        profile.getTopGenres().clear();
        List<GenreCounter> genreCounters = new ArrayList<>();

        HashMap<String, Integer> genreOccurences = new HashMap<>();
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
        List<String> firstNElementsList = genreCounters
                .stream()
                .limit(10)
                .map(genreCounter -> genreCounter.genre)
                .collect(Collectors.toList());

        for (String genreName : firstNElementsList) {
            Optional<Genre> genre = genreRepository.findByName(genreName);
            if (genre.isEmpty()) {
                Genre newGenre = new Genre();
                newGenre.setName(genreName);
                newGenre.addProfile(profile);
                genreRepository.save(newGenre);
            } else {
                genre.get().addProfile(profile);
                genreRepository.save(genre.get());
            }
        }
    }

    public Set<GenreDto> getTopGenres(Account account) {
        return account.getProfile().getTopGenres()
                .stream()
                .map(genreDtoMapper::entityToDto)
                .collect(Collectors.toSet());
    }

    @AllArgsConstructor
    @Getter
    static class GenreCounter {
        private String genre;
        private int counter;
    }
}
