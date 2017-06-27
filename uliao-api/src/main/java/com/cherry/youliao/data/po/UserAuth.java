package com.cherry.youliao.data.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nutz.dao.entity.annotation.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("t_user_auths")
@TableIndexes(
        @Index(name = "uk_1", fields = {"identityType", "identifier"})
)
public class UserAuth {
    /**
     * 标识类型, 详见 IdentityType  WEIBO, WEIXIN, EMAIL, USERNAME, MOBILE, QQ
     */
    @Column("identity_type")
    @ColDefine(width = 16, notNull = true)
    private String identityType;
    /**
     * 用户的标识
     */
    @Column
    @ColDefine(width = 64, notNull = true)
    private String identifier;
    /**
     * 凭据, 可能是密码hash,access_token
     */
    @Column
    @ColDefine(width = 64, notNull = true)
    private String credential;
    /**
     * 绑定的用户ID
     */
    @Column("user_id")
    @ColDefine(notNull = true)
    private Long userId;

    @Column
    private boolean verified;
}



