package com.cherry.youliao.service.exception;

public class EmailWasNotVerifiedException extends Exception {

    public EmailWasNotVerifiedException() {
        super("电子邮箱尚未完成验证");
    }

}

