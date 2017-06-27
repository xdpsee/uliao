package com.cherry.youliao.data.service.exception;

public class UserNameExistsException extends Exception {

    public UserNameExistsException() {
        super("用户名(邮箱)已被使用");
    }

}


