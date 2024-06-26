package com.edu.basaoc.service;

import com.edu.basaoc.model.ArtistDto;
import com.edu.basaoc.model.GenderType;
import com.edu.basaoc.model.ProfileRequestDto;
import com.edu.basaoc.model.entity.Account;
import com.edu.basaoc.model.entity.Artist;
import com.edu.basaoc.model.entity.Profile;
import com.edu.basaoc.model.mapper.ArtistDtoMapper;
import com.edu.basaoc.model.mapper.GenreDtoMapper;
import com.edu.basaoc.model.mapper.ProfileResponseDtoMapper;
import com.edu.basaoc.model.repository.ArtistRepository;
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
    private final GenreService genreService;

    public ProfileService(ArtistRepository artistRepository, ProfileRepository profileRepository, GenreService genreService) {
        this.artistRepository = artistRepository;
        this.profileRepository = profileRepository;
        this.genreService = genreService;
    }

    public Profile createProfile(Account account, se.michaelthelin.spotify.model_objects.specification.Artist[] spotifyArtists, List<String> genres, String profileImageUrl, double[] mdnValues) {
        Profile profile = new Profile();
        profile.setAccount(account);
        profileRepository.save(profile);
        profile.setProfilePictureUrl(profileImageUrl);
        setTopArtists(profile, spotifyArtists);
        genreService.setTopGenres(profile, genres);
        profile.setAge(0);
        profile.setName("");
        profile.setContactInfo("");
        profile.setBio("");
        profile.setGenderType(null);
        profile.setMainstreamFactor(mdnValues[0]);
        profile.setDiverseFactor(mdnValues[1]);
        profile.setNovelFactor(mdnValues[2]);
        return profileRepository.save(profile);
    }

    public Profile updateProfile(Profile profile, ProfileRequestDto requestDto) {
        if ( requestDto.getName() == null &&
        requestDto.getAge() == 0 &&
        requestDto.getContactInfo() == null &&
        requestDto.getBio() == null &&
        requestDto.getGenderType() == null) {
            profile.setLatitude(requestDto.getLatitude());
            profile.setLongitude(requestDto.getLongitude());
            return profileRepository.save(profile);
        }
        profile.setName(requestDto.getName());
        profile.setAge(requestDto.getAge());
        profile.setContactInfo(requestDto.getContactInfo());
        profile.setBio(requestDto.getBio());
        profile.setGenderType(requestDto.getGenderType());
        return profileRepository.save(profile);
    }

    public Profile updateProfileTopValues(Profile profile, se.michaelthelin.spotify.model_objects.specification.Artist[] spotifyArtists, List<String> genres) {
        setTopArtists(profile, spotifyArtists);
        genreService.setTopGenres(profile, genres);
        return profileRepository.save(profile);
    }

    public void updateProfileImageUrl(Profile profile, String profileImageUrl) {
        profile.setProfilePictureUrl(profileImageUrl);
        profileRepository.save(profile);
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

    public Profile getProfileById(Long profileId) {
        return profileRepository.findByProfileId(profileId).orElseThrow();
    }

    public Profile getProfile(Account account) {
        return profileRepository.findByAccount(account);
    }
}
