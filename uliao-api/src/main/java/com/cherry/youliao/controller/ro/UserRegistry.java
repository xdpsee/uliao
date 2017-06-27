package com.cherry.youliao.controller.ro;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserRegistry {

    @NotNull @Length(min = 6, max = 64, message = "用户名长度必须大于6个字符")
    private String identifier;

    @NotNull @Length(min = 8, max = 64, message = "密码长度至少8个字符")
    private String password;

    private String inviteCode;

}



