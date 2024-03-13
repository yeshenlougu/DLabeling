package com.dlabeling.system.domain.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/2
 */
@Data
public class LevelApply implements Serializable {


    private static final long serialVersionUID = -8326279064456737173L;

    private int id;

    private int applyer;

    private int type;

    private int privilege;

    private int status;

    private int judger;

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
