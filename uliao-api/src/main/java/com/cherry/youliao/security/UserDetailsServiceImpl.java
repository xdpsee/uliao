package com.cherry.youliao.security;

import com.cherry.youliao.data.enums.IdentityType;
import com.cherry.youliao.data.po.Authority;
import com.cherry.youliao.data.po.User;
import com.cherry.youliao.data.po.UserAuth;
import com.cherry.youliao.data.repository.Repository;
import org.nutz.dao.Cnd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Deprecated
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private Repository repository;

    @Override
    public UserDetails loadUserByUsername(String principal) throws UsernameNotFoundException {

        final String[] components = principal.split(":");
        final IdentityType identityType = IdentityType.valueOf(components[0]);
        final String identifier = components[1];

        UserAuth userAuth = repository.fetch(UserAuth.class
                , Cnd.where("identity_type", "=", identityType)
                        .and("identifier", "=", identifier));
        User user = null;
        if (userAuth != null) {
            user = repository.fetch(User.class, Cnd.where("id", "=", userAuth.getUserId()));
        }

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        } else {
            List<Authority> authorities = repository.query(Authority.class
                    , Cnd.where("user_id", "=", userAuth.getUserId()));
            return UserDetailsFactory.create(user, userAuth, authorities);
        }
    }
}

