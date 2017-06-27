package com.cherry.youliao.service;

import com.cherry.youliao.data.po.UserAuth;

public interface EmailService {

    boolean sendVerifyEmail(UserAuth user, String link);

}


