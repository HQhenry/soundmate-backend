package com.edu.basaoc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import se.michaelthelin.spotify.model_objects.specification.Image;

@Getter
@Setter
@AllArgsConstructor
public class ArtistDto {
    private String name;
    private String imageUrl;
}
