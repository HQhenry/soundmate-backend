package com.edu.basaoc.model.entity;


import com.edu.basaoc.model.ListeningType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "match")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long matchId;

    @Setter
    @Column(name = "date")
    private LocalDate matchDate;

    @Setter
    @Column(name = "matched_on_type")
    private String matchedOnType;


    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_1_fk", referencedColumnName = "profile_id")
    private Profile profile1;

    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_2_fk", referencedColumnName = "profile_id")
    private Profile profile2;


}
