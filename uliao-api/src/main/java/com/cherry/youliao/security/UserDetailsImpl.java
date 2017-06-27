package com.cherry.youliao.security;

import com.cherry.youliao.data.enums.IdentityType;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

@Deprecated
@Data
public class UserDetailsImpl implements UserDetails {

    private final Long id;

    private final IdentityType identityType;

    private final String identifier;

    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id
            , IdentityType identityType
            , String identifier
            , String password
            , Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.identityType = identityType;
        this.identifier = identifier;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return String.format("%s:%s", identityType, identifier);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

