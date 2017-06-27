package com.cherry.youliao.security;

import com.cherry.youliao.data.po.UserAuth;
import com.cherry.youliao.data.repository.Repository;
import org.nutz.dao.Cnd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private Repository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {

        UserAuthenticationToken authentication = (UserAuthenticationToken) auth;

        final UserAuth userAuth = repository.fetch(UserAuth.class
                , Cnd.where("identity_type", "=", authentication.getIdentityType().name())
                        .and("identifier", "=", authentication.getIdentifier()));
        if (userAuth == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        if (!passwordEncoder.matches((String)authentication.getCredentials(), userAuth.getCredential())) {
            throw new BadCredentialsException("密码错误");
        }

        authentication.setUserId(userAuth.getUserId());

        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UserAuthenticationToken.class);
    }
}
