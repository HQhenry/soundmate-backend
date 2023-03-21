package com.edu.basaoc.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "artist")
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artistId")
    private Long artistId;

    @Setter
    private String name;

    @Setter
    @Column(name = "image_url")
    private String imageUrl;

    @ManyToMany
    Set<Profile> profiles = new HashSet<>();

    public void addProfile(Profile profile) {
        profiles.add(profile);
        profile.getTopArtists().add(this);
    }

}
