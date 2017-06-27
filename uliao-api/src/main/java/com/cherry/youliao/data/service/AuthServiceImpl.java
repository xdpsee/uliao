package com.cherry.youliao.data.service;

import com.cherry.youliao.controller.manager.RegistryManager;
import com.cherry.youliao.controller.ro.UserRegistry;
import com.cherry.youliao.data.cache.JwtTokenCache;
import com.cherry.youliao.data.enums.IdentityType;
import com.cherry.youliao.data.po.Authority;
import com.cherry.youliao.data.repository.Repository;
import com.cherry.youliao.data.service.exception.*;
import com.cherry.youliao.security.UserAuthenticationToken;
import com.cherry.youliao.security.jwt.JwtTokenUtils;
import com.cherry.youliao.utils.ObjectValidator;
import org.nutz.dao.Cnd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class AuthServiceImpl implements AuthService {
    // Http request header
    private static final String AUTHORIZATION = "Authorization";
    // Authorization value head
    private static final String BEARER = "Bearer ";

    private static final String EMAIL_PATTERN = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";

    private static final String MOBILE_PATTERN = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\\\d{8}$";

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private Repository repository;
    @Autowired
    private ObjectValidator objectValidator;
    @Autowired
    private JwtTokenCache jwtTokenCache;
    @Autowired
    private RegistryManager registryManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(UserRegistry registry) throws EmailExistsException, UserNameExistsException, InviteCodeNotFoundException, InviteCodeWasUsedException, InvalidParamsException {

        objectValidator.check(registry);

        if (registry.getIdentifier().matches(EMAIL_PATTERN)) {
            registryManager.registryByEmail(registry.getIdentifier()
                    , registry.getIdentifier() // 昵称默认
                    , registry.getPassword()
                    , registry.getInviteCode());
        } else if (registry.getIdentifier().matches(MOBILE_PATTERN)) {
            registryManager.registryByMobile(registry.getIdentifier()
                    , registry.getIdentifier() // 昵称默认
                    , registry.getPassword()
                    , registry.getInviteCode());
        } else {
            registryManager.registryByUsername(registry.getIdentifier()
                    , registry.getPassword()
                    , registry.getInviteCode());
        }
    }

    @Override
    public String login(final String identifier, String credential) throws EmailWasNotVerifiedException {

        IdentityType identityType = IdentityType.USERNAME;
        if (identifier.matches(EMAIL_PATTERN)) {
            identityType = IdentityType.EMAIL;
        }
        if (identifier.matches(MOBILE_PATTERN)) {
            identityType = IdentityType.MOBILE;
        }

        UserAuthenticationToken upToken = new UserAuthenticationToken(identityType, identifier, credential);

        final Authentication authentication = authenticationManager.authenticate(upToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String principal = upToken.getPrincipal().toString();
        String token = jwtTokenCache.get(upToken.getPrincipal().toString());
        if (!StringUtils.isEmpty(token)) {
            return token;
        }

        List<Authority> authorities = repository.query(Authority.class, Cnd.where("user_id", "=", upToken.getUserId()));
        token = jwtTokenUtils.generateToken(principal, authorities.stream()
                .map(e -> new SimpleGrantedAuthority(e.getRole())).collect(toList()));
        if (token != null) {
            jwtTokenCache.put(principal, token, jwtTokenUtils.getExpiresDateFromToken(token));
        }

        return token;
    }

    @Override
    public String refresh(String bearer) {
        final String authToken = bearer.substring(BEARER.length());
        final String principal = jwtTokenUtils.getPrincipalFromToken(authToken);
        final String cachedToken = jwtTokenCache.get(principal);
        if (cachedToken != null && cachedToken.equals(authToken)) {
            String refreshedToken = jwtTokenUtils.refreshToken(authToken);
            if (refreshedToken != null) {
                jwtTokenCache.put(principal, refreshedToken, jwtTokenUtils.getExpiresDateFromToken(refreshedToken));
                return refreshedToken;
            }
        }

        return null;
    }

}




