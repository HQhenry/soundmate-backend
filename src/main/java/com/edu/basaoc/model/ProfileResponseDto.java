package com.edu.basaoc.model;

import com.edu.basaoc.model.entity.Artist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class ProfileResponseDto {

    private long profileId;
    private String name;
    private long age;
    private String contactInfo;
    private Set<ArtistDto> topArtists;
    private Set<GenreDto> topGenres;
    private String profilePictureUrl;
    private String bio;
    private GenderType genderType;
}
