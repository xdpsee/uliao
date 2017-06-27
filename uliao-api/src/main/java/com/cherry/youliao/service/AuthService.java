package com.cherry.youliao.service;


import com.cherry.youliao.api.ro.UserRegistry;
import com.cherry.youliao.service.exception.*;

public interface AuthService {

    void register(UserRegistry registry) throws EmailExistsException
            , UserNameExistsException
            , InviteCodeNotFoundException
            , InviteCodeWasUsedException
            , InvalidParamsException;

    String login(final String identifier, String credential) throws EmailWasNotVerifiedException;


    String refresh(String bearer);
}

