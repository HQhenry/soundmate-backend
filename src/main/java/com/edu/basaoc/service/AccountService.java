package com.edu.basaoc.service;

import com.edu.basaoc.model.AccountDto;
import com.edu.basaoc.model.mapper.AccountDtoMapper;
import com.edu.basaoc.persistence.entity.Account;
import com.edu.basaoc.persistence.repository.AccountRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
public class AccountService {

    private static final AccountDtoMapper mapper = Mappers.getMapper(AccountDtoMapper.class);

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public AccountDto createOrUpdateUser(AccountDto accountDto) {
        Account account;
        String spotifyUserId = new SpotifyDataService().fetchUserId(
                accountDto.getAccessToken(), accountDto.getRefreshToken()
        );
        if (!repository.existsBySpotifyUserId(spotifyUserId)) {
            account = mapper.dtoToAccount(accountDto);
        } else {
            account = repository.findBySpotifyUserId(spotifyUserId).get();
            account.setAccessToken(accountDto.getAccessToken());
            account.setRefreshToken(accountDto.getRefreshToken());
            account.setAccessExpiresOn(accountDto.getAccessExpiresOn());
        }
        repository.save(account);
        return mapper.accountToDto(account);
    }

    public AccountDto loadUserById(Long aLong) {
        return mapper.accountToDto(repository.getById(aLong));
    }
}
