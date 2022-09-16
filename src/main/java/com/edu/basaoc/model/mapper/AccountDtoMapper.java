package com.edu.basaoc.model.mapper;

import com.edu.basaoc.model.AccountDto;
import com.edu.basaoc.persistence.entity.Account;
import org.mapstruct.Mapper;

@Mapper
public interface AccountDtoMapper {

    Account dtoToAccount(AccountDto accountDto);
    AccountDto accountToDto(Account account);

}
