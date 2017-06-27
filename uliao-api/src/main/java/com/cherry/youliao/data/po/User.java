package com.cherry.youliao.data.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.nutz.dao.entity.annotation.*;

import java.util.Date;

@Data
@Table("t_users")
@NoArgsConstructor
public class User {
    @Id
    private Long id;

    @Name
    @ColDefine(width = 64, notNull = true)
    private String nickname;

    @Column
    @ColDefine(width = 256)
    private String avatar;

}




