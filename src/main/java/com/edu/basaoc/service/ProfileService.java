package com.edu.basaoc.service;

import com.edu.basaoc.model.ArtistDto;
import com.edu.basaoc.model.GenreDto;
import com.edu.basaoc.model.ProfileResponseDto;
import com.edu.basaoc.model.entity.Account;
import com.edu.basaoc.model.entity.Artist;
import com.edu.basaoc.model.entity.Genre;
import com.edu.basaoc.model.entity.Profile;
import com.edu.basaoc.model.mapper.ArtistDtoMapper;
import com.edu.basaoc.model.mapper.GenreDtoMapper;
import com.edu.basaoc.model.mapper.ProfileResponseDtoMapper;
import com.edu.basaoc.model.repository.ArtistRepository;
import com.edu.basaoc.model.repository.GenreRepository;
import com.edu.basaoc.model.repository.ProfileRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    private static final ArtistDtoMapper mapper = Mappers.getMapper(ArtistDtoMapper.class);
    static final GenreDtoMapper genreDtoMapper = Mappers.getMapper(GenreDtoMapper.class);
    private static final ProfileResponseDtoMapper profileResponseDtoMapper = Mappers.getMapper(ProfileResponseDtoMapper.class);



    private final ArtistRepository artistRepository;
    private final ProfileRepository profileRepository;
    private final GenreRepository genreRepository;

    public ProfileService(ArtistRepository artistRepository, ProfileRepository profileRepository, GenreRepository genreRepository) {
        this.artistRepository = artistRepository;
        this.profileRepository = profileRepository;
        this.genreRepository = genreRepository;
    }

    public ProfileResponseDto getProfile(Account account) {
    Profile profile = profileRepository.findByAccount(account);
    return profileResponseDtoMapper.entityToDto(profile);
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
}
