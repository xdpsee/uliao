package com.cherry.youliao.controller;

import com.cherry.youliao.controller.vo.ResponseData;
import com.cherry.youliao.data.enums.IdentityType;
import com.cherry.youliao.data.service.AuthService;
import com.cherry.youliao.data.service.exception.EmailWasNotVerifiedException;
import com.cherry.youliao.data.service.exception.InvalidParamsException;
import com.cherry.youliao.security.jwt.JwtAuthenticationRequest;
import com.cherry.youliao.security.jwt.JwtAuthenticationResponse;
import com.cherry.youliao.utils.ObjectValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

@RestController
public class AuthController {

    // Http request header
    private static final String AUTHORIZATION = "Authorization";

    @Autowired
    private AuthService authService;
    @Autowired
    private ObjectValidator objectValidator;

    @RequestMapping(value = "/auth/token"
            , method = RequestMethod.POST
            , produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public ResponseData<?> createToken(@RequestBody JwtAuthenticationRequest authenticationRequest) {
        try {
            objectValidator.check(authenticationRequest);

            final String token = authService.login(authenticationRequest.getIdentifier(), authenticationRequest.getPassword());
            return ResponseData.success(new JwtAuthenticationResponse(token));

        } catch (InvalidParamsException e) {
            return ResponseData.error(e.getMessage());
        } catch (EmailWasNotVerifiedException e) {
            return ResponseData.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/auth/refresh-token"
            , method = RequestMethod.GET
            , produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public ResponseEntity<?> refreshToken(
            HttpServletRequest request) throws AuthenticationException {
        String bearer = request.getHeader(AUTHORIZATION);
        String refreshedToken = authService.refresh(bearer);
        if (refreshedToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        }
    }

}

