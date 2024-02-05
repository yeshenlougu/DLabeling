package com.dlabeling.system.domain.po.user;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

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
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 删除时间
     */
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date destroyTime;
}
