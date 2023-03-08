package com.edu.basaoc.model.mapper;

import com.edu.basaoc.model.entity.Account;
import com.edu.basaoc.security.ApplicationUserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ApplicationUserDetailsMapper {

    ApplicationUserDetails accountToUserDetails(Account account);

    Account userDetailsToAccount(ApplicationUserDetails userDetails);

    static ApplicationUserDetailsMapper getInstance() {
        return Mappers.getMapper(ApplicationUserDetailsMapper.class);
    }

}
