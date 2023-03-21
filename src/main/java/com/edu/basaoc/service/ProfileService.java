package com.edu.basaoc.service;

import com.edu.basaoc.model.ArtistDto;
import com.edu.basaoc.model.entity.Account;
import com.edu.basaoc.model.entity.Artist;
import com.edu.basaoc.model.entity.Profile;
import com.edu.basaoc.model.mapper.ArtistDtoMapper;
import com.edu.basaoc.model.repository.ArtistRepository;
import com.edu.basaoc.model.repository.ProfileRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    private static final ArtistDtoMapper mapper = Mappers.getMapper(ArtistDtoMapper.class);

    private final ArtistRepository artistRepository;
    private final ProfileRepository profileRepository;

    public ProfileService(ArtistRepository artistRepository, ProfileRepository profileRepository) {
        this.artistRepository = artistRepository;
        this.profileRepository = profileRepository;
    }

    public Profile getProfile(Account account) {
        return profileRepository.findByAccount(account);
    }
    public void setTopArtist(ArtistDto artistDto, Account account) {
        Optional<Artist> artist = artistRepository.findByName(artistDto.getName());
        if (artist.isEmpty()) {
            Artist newArtist = new Artist();
            newArtist.setName(artistDto.getName());
            newArtist.setImageUrl(artistDto.getImageUrl());
            newArtist.addProfile(profileRepository.findByAccount(account));
        } else {
            artist.get().addProfile(profileRepository.findByAccount(account));
        }
    }

    public Set<ArtistDto> getTopArtists(Account account) {
        Profile profile = profileRepository.findByAccount(account);
        return profile.getTopArtists().stream().map(mapper::entityToDto).collect(Collectors.toSet());
    }
}
