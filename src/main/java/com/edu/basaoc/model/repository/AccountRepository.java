package com.edu.basaoc.model.repository;

import com.edu.basaoc.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);
    Optional<Account> findByPassword(String password);

    //For testing purposes
    Optional<Account> findByAccountId(Long accountId);
}
