package com.edu.basaoc.security;

import java.util.ArrayList;
import java.util.Optional;

import com.edu.basaoc.model.AccountDto;
import com.edu.basaoc.persistence.entity.Account;
import com.edu.basaoc.persistence.repository.AccountRepository;
import com.edu.basaoc.service.AccountService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final AccountService accountService;

    public JwtUserDetailsService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountDto accountDto = accountService.loadUserById(Long.parseLong(username));
        if (accountDto == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new User(accountDto.getId().toString(), accountDto.getAccessToken(), new ArrayList<>());
    }

    public AccountDto save(AccountDto accountDto) {
        //TODO encrypt access token?
        return accountService.createOrUpdateUser(accountDto);
    }
}
