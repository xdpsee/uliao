package com.cherry.youliao.controller.misc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerifyToken {

    private String email;

    private Long expiresAt;

}
