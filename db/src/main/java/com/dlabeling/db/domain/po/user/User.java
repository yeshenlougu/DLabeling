package com.dlabeling.db.domain.po.user;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/2
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 5154580971876896768L;

    /**
     * 用户id
     */
    private Integer id;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除时间
     */
    private Date destroyTime;
}
