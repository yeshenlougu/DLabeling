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
public class Datasets implements Serializable {

    private static final long serialVersionUID = -5366224950151388405L;

    /**
     * 数据集编号
     */
    private Integer id;

    /**
     * 数据集名称
     */
    private String name;

    /**
     * 数据集描述
     */
    private String description;

    /**
     * 数据集类型
     */
    private Integer type;

    /**
     * 数据存储目录
     */
    private String dataRootDir;


    /**
     * 数据集可见性
     */
    private Boolean visible;

    /**
     * 数据集创建时间
     */
    private Date createTime;

    /**
     * 数据集更新时间
     */
    private Date updateTime;

    /**
     * 数据集删除时间
     */
    private Date destroyTime;

}
