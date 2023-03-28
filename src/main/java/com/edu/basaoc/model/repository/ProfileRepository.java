package com.edu.basaoc.model.repository;

import com.edu.basaoc.model.entity.Account;
import com.edu.basaoc.model.entity.Artist;
import com.edu.basaoc.model.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByAccount(Account account);
    //For testing purposes
    Optional<Profile> findByProfileId(Long profileId);

}
