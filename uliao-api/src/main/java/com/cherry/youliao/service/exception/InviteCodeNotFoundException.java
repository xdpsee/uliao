package com.cherry.youliao.service.exception;

public class InviteCodeNotFoundException extends Exception {
    public InviteCodeNotFoundException() {
        super("无效的邀请码");
    }
}
