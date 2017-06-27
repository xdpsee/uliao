package com.cherry.youliao.data.enums;

public enum IdentityType {

    WEIBO(1, "新浪微博"),
    WEIXIN(2, "微信"),
    MOBILE(3, "手机"),
    EMAIL(4, "电子邮件"),
    USERNAME(5, "用户名"),
    QQ(6, "QQ号码"),;

    public final int code;
    public final String comment;

    IdentityType(int code, String comment) {
        this.code = code;
        this.comment = comment;
    }
}


