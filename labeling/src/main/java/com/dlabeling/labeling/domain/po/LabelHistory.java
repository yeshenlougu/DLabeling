package com.dlabeling.labeling.domain.po;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;
/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
@Data
public class LabelHistory implements Serializable {


    private static final long serialVersionUID = 8286325244940963506L;

    /**
     * id标识
     */
    private Integer id;

    /**
     * 操作者id
     */
    private Integer userId;

    /**
     * 数据集id
     */
    private Integer datasetId;

    /**
     * 被标注数据id
     */
    private Integer dataId;

    /**
     * 数据操作类型
     */
    private Integer actionType;

    /**
     * 修改前
     */
    private String beforeAction;

    /**
     * 修改后
     */
    private String afterAction;

    /**
     * 标注时间
     */
    private Date createTime;

    /**
     * 是否删除
     */
    private Boolean destroy;
}
