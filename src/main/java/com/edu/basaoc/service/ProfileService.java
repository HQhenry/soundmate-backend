package com.edu.basaoc.service;

import com.edu.basaoc.model.ArtistDto;
import com.edu.basaoc.model.GenreDto;
import com.edu.basaoc.model.ProfileRequestDto;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
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
    private final GenreService genreService;

    public ProfileService(ArtistRepository artistRepository, ProfileRepository profileRepository, GenreService genreService) {
        this.artistRepository = artistRepository;
        this.profileRepository = profileRepository;
        this.genreService = genreService;
    }

    public Profile createProfile(Account account, se.michaelthelin.spotify.model_objects.specification.Artist[] spotifyArtists, List<String> genres) {
        Profile profile = new Profile();
        profile.setAccount(account);
        profileRepository.save(profile);
        setTopArtists(profile, spotifyArtists);
        genreService.setTopGenres(profile, genres);
        return profileRepository.save(profile);
    }

    public Profile updateProfile(Profile profile, ProfileRequestDto requestDto) {
        profile.setName(requestDto.getName());
        profile.setAge(requestDto.getAge());
        profile.setContactInfo(requestDto.getContactInfo());
        return profileRepository.save(profile);
    }

    public Profile updateProfileTopValues(Profile profile, se.michaelthelin.spotify.model_objects.specification.Artist[] spotifyArtists, List<String> genres) {
        setTopArtists(profile, spotifyArtists);
        genreService.setTopGenres(profile, genres);
        return profileRepository.save(profile);
    }

    public void setTopArtists(Profile profile, se.michaelthelin.spotify.model_objects.specification.Artist[] spotifyArtists) {
        profile.getTopArtists().clear();
        for (se.michaelthelin.spotify.model_objects.specification.Artist spotifyArtist :
                spotifyArtists) {
            Optional<Artist> artistOptional = artistRepository.findByName(spotifyArtist.getName());
            if (artistOptional.isEmpty()) {
                Artist newArtist = new Artist();
                newArtist.setName(spotifyArtist.getName());
                newArtist.setImageUrl(spotifyArtist.getImages()[0].getUrl());

                newArtist.addProfile(profile);
                artistRepository.save(newArtist);
            } else {
                Artist artist = artistOptional.get();
                artist.addProfile(profile);
                artistRepository.save(artist);
            }
        }

    }

    public Set<ArtistDto> getTopArtists(Account account) {
        Profile profile = profileRepository.findByAccount(account);
        return profile.getTopArtists().stream().map(mapper::entityToDto).collect(Collectors.toSet());
    }

    public String getProfilePictureUrl(Account account) {
        return getProfilePictureUrl(account);
    }

public Profile getProfileById(Long profileId) {
        return profileRepository.findByProfileId(profileId).orElseThrow();
    }

}
