package com.cherry.youliao.data.po;

import lombok.Data;
import org.nutz.dao.entity.annotation.*;

@Data
@Table("t_invite_codes")
@TableIndexes(
        @Index(name = "uk_1", fields = "inviteCode")
)
public class InviteCode {

    @Column("invite_code")
    @ColDefine(width = 64, notNull = true)
    private String inviteCode;

    @Column("from_user_id")
    private Long fromUserId = 0L;

    @Column("used_user_id")
    private Long usedUserId = 0L;

    @Column
    private Long version = 0L;

}



