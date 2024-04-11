package com.edu.basaoc.model.entity;

import com.edu.basaoc.model.GenderType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")

    private long profileId;
    @Setter
    @Column(name = "name")
    private String name;
    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_fk", referencedColumnName = "account_id")
    private Account account;
    @Setter
    @Column(name = "age")
    private long age;
    @Setter
    @Column(name = "contact_info")
    private String contactInfo;
    @Setter
    @Column(name = "novel_factor")
    private double novelFactor;
    @Setter
    @Column(name = "mainstream_factor")
    private double mainstreamFactor;
    @Setter
    @Column(name = "diverse_factor")
    private double diverseFactor;

    @Setter
    @Column(name = "profile_image_url")
    private String profilePictureUrl;

    @Setter
    @Column(name = "bio")
    private String bio;

    @Setter
    @Column(name = "gender_type")
    @Enumerated(EnumType.STRING)
    private GenderType genderType;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(
            name = "top_artists",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    @OrderBy("name")
    private Set<Artist> topArtists = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(
            name = "top_genres",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> topGenres = new HashSet<>();

    @Setter
    @Column(name = "latitude")
    private Double latitude;

    @Setter
    @Column(name = "longitude")
    private Double longitude;

    public void addTopArtist(Artist artist) {
        topArtists.add(artist);
        artist.getProfiles().add(this);
    }

    public void removeTopArtist(Artist artist) {
        topArtists.remove(artist);
        artist.getProfiles().remove(this);
    }

    public void addTopGenre(Genre genre) {
        topGenres.add(genre);
        genre.getProfiles().add(this);
    }

    public void removeTopGenre(Genre genre) {
        topGenres.remove(genre);
        genre.getProfiles().remove(this);
    }
}

