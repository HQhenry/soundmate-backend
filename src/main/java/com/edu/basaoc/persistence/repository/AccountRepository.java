package com.edu.basaoc.persistence.repository;

import com.edu.basaoc.persistence.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findBySpotifyUserId(String spotifyUserId);

    boolean existsBySpotifyUserId(String spotifyUserId);

}
