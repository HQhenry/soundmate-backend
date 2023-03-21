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
    @Column(name = "info_text")
    private String infoText;
    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_fk", referencedColumnName = "account_id")
    private Account account;
    //TODO: eventuell weg
    @Setter
    private String spotifyUserId;
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

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(
            name = "top_artists",
            joinColumns = @JoinColumn(name = "profile_fk"),
            inverseJoinColumns = @JoinColumn(name = "artist_fk")
    )
    Set<Artist> topArtists = new HashSet<>();

    public void addTopArtist(Artist artist) {
        topArtists.add(artist);
        //artist.getProfiles().add(this);
    }
}
