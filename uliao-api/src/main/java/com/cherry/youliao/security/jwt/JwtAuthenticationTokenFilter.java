package com.cherry.youliao.security.jwt;

import com.cherry.youliao.data.cache.JwtTokenCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    // Http request header
    private static final String AUTHORIZATION = "Authorization";
    // Authorization value head
    private static final String BEARER = "Bearer ";
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private JwtTokenCache jwtTokenCache;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(BEARER)) {
            final String authToken = authHeader.substring(BEARER.length()); // The part after "Bearer "
            String principal = jwtTokenUtils.getPrincipalFromToken(authToken);

            logger.info("Authentication checking principal: " + principal);

            if (principal != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                final String cachedToken = jwtTokenCache.get(principal);
                if (cachedToken != null && cachedToken.equals(authToken)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            principal, null, jwtTokenUtils.getAuthoritiesFromToken(cachedToken));
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    logger.info("Authentication principal: " + principal + " authenticated");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        chain.doFilter(request, response);
    }
}

