package com.cherry.youliao.security;

import com.cherry.youliao.data.enums.IdentityType;
import com.cherry.youliao.data.po.Authority;
import com.cherry.youliao.data.po.User;
import com.cherry.youliao.data.po.UserAuth;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;
@Deprecated
public final class UserDetailsFactory {

    public static UserDetailsImpl create(User user, UserAuth userAuth, List<Authority> authorities) {
        return new UserDetailsImpl(
                user.getId(),
                IdentityType.valueOf(userAuth.getIdentityType()),
                userAuth.getIdentifier(),
                userAuth.getCredential(),
                mapToGrantedAuthorities(authorities)
        );
    }

    public static String joinAuthority(Collection<? extends GrantedAuthority> authorities) {
        String[] roles = authorities.stream()
                .map(GrantedAuthority::getAuthority).collect(toList())
                .toArray(new String[0]);

        return StringUtils.join(roles, ",");
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Authority> authorities) {
        return authorities.stream()
                .map(e -> new SimpleGrantedAuthority(e.getRole()))
                .collect(toList());
    }

}


