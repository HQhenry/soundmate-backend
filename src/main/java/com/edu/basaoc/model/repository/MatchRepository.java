package com.edu.basaoc.model.repository;

import com.edu.basaoc.model.entity.Genre;
import com.edu.basaoc.model.entity.Match;
import com.edu.basaoc.model.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    Optional<List<Match>> findAllByProfile1(Profile profile);
    Optional<List<Match>>  findAllByProfile2(Profile profile);
}
