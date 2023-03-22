package com.edu.basaoc.service;

import com.edu.basaoc.model.ArtistDto;
import com.edu.basaoc.model.GenreDto;
import com.edu.basaoc.model.entity.Account;
import com.edu.basaoc.model.entity.Artist;
import com.edu.basaoc.model.entity.Genre;
import com.edu.basaoc.model.entity.Profile;
import com.edu.basaoc.model.mapper.ArtistDtoMapper;
import com.edu.basaoc.model.mapper.GenreDtoMapper;
import com.edu.basaoc.model.repository.ArtistRepository;
import com.edu.basaoc.model.repository.GenreRepository;
import com.edu.basaoc.model.repository.ProfileRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    private static final ArtistDtoMapper mapper = Mappers.getMapper(ArtistDtoMapper.class);
    private static final GenreDtoMapper genreDtoMapper = Mappers.getMapper(GenreDtoMapper.class);


    private final ArtistRepository artistRepository;
    private final ProfileRepository profileRepository;
    private final GenreRepository genreRepository;

    public ProfileService(ArtistRepository artistRepository, ProfileRepository profileRepository, GenreRepository genreRepository) {
        this.artistRepository = artistRepository;
        this.profileRepository = profileRepository;
        this.genreRepository = genreRepository;
    }

    public Profile getProfile(Account account) {
        return profileRepository.findByAccount(account);
    }
    public void setTopArtist(ArtistDto artistDto, Account account) {
        //TODO: remove top artists of user
        Optional<Artist> artist = artistRepository.findByName(artistDto.getName());
        if (artist.isEmpty()) {
            Artist newArtist = new Artist();
            newArtist.setName(artistDto.getName());
            newArtist.setImageUrl(artistDto.getImageUrl());
            newArtist.addProfile(profileRepository.findByAccount(account));
            System.out.println(profileRepository.findByAccount(account).getProfileId());
            artistRepository.save(newArtist);
        } else {
            artist.get().addProfile(profileRepository.findByAccount(account));
            artistRepository.save(artist.get());
        }
    }

    public Set<ArtistDto> getTopArtists(Account account) {
        Profile profile = profileRepository.findByAccount(account);
        return profile.getTopArtists().stream().map(mapper::entityToDto).collect(Collectors.toSet());
    }

    public void setTopGenre(GenreDto genreDto, Account account) {
        //TODO: remove top genres of user
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

    public Set<GenreDto> getTopGenres(Account account) {
        Profile profile = profileRepository.findByAccount(account);
        return profile.getTopGenres().stream().map(genreDtoMapper::entityToDto).collect(Collectors.toSet());
    }
}
