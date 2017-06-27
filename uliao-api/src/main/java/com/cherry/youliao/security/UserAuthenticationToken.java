package com.cherry.youliao.security;

import com.cherry.youliao.data.enums.IdentityType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.ArrayList;

public class UserAuthenticationToken extends AbstractAuthenticationToken {

    @Getter
    private final IdentityType identityType;
    @Getter
    private final String identifier;
    @Getter
    private String credentials;

    @Setter @Getter
    private Long userId;

    public UserAuthenticationToken(IdentityType identityType
            , String identifier
            , String credentials) {
        super(new ArrayList<>());
        this.identityType = identityType;
        this.identifier = identifier;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return String.format("%s:%s", identityType, identifier);
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        credentials = null;
    }
}

