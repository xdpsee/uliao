package com.cherry.youliao.service.exception;

public class EmailExistsException extends Exception {

    public EmailExistsException() {
        super("邮箱已注册");
    }

}
