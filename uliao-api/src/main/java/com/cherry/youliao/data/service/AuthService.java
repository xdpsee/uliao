package com.cherry.youliao.data.service;


import com.cherry.youliao.controller.ro.UserRegistry;
import com.cherry.youliao.data.service.exception.*;

public interface AuthService {

    void register(UserRegistry registry) throws EmailExistsException
            , UserNameExistsException
            , InviteCodeNotFoundException
            , InviteCodeWasUsedException
            , InvalidParamsException;

    String login(final String identifier, String credential) throws EmailWasNotVerifiedException;

    String refresh(String bearer);
}

