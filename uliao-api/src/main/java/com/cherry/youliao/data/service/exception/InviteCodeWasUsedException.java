package com.cherry.youliao.data.service.exception;

public class InviteCodeWasUsedException extends Exception {

    public InviteCodeWasUsedException() {
        super("邀请码已被使用");
    }

}
