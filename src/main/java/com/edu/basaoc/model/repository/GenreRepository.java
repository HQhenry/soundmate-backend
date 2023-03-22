package com.edu.basaoc.model.repository;

import com.edu.basaoc.model.entity.Artist;
import com.edu.basaoc.model.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByName(String name);
}
