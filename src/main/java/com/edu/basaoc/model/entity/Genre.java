package com.edu.basaoc.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "genre")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    private Long genreId;

    @Setter
    private String name;
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "topGenres")
    @JsonIgnore
    private Set<Profile> profiles = new HashSet<>();

    public void addProfile(Profile profile) {
        profiles.add(profile);
        profile.getTopGenres().add(this);
    }

    public void removeProfile(Profile profile) {
        profiles.remove(profile);
        profile.getTopGenres().remove(this);
    }
}
