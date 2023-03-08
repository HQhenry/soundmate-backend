package com.edu.basaoc.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@AllArgsConstructor
public class ApplicationUserDetails implements UserDetails {

    private static final long serialVersionUID = 2;

    private final boolean accountNonExpired = true;
    private final boolean accountNonLocked = true;
    private final boolean credentialsNonExpired = true;
    private final boolean enabled = true;

    private String password;
    private String username;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptySet();
    }
}
