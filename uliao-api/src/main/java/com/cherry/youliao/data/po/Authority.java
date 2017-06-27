package com.cherry.youliao.data.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nutz.dao.entity.annotation.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("t_user_roles")
@TableIndexes(
        @Index(name = "uk_1", fields = {"userId", "role"})
)
public class Authority {
    @Column("user_id")
    @ColDefine(notNull = true)
    private Long userId;

    @Column
    @ColDefine(width = 16, notNull = true)
    private String role;
}


