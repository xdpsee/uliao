package com.cherry.youliao.data.service;

import com.cherry.youliao.data.po.User;
import com.cherry.youliao.data.po.UserAuth;

public interface EmailService {

    boolean sendVerifyEmail(UserAuth user, String link);

}


