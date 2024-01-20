package com.labeling.labeling.domain.po;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

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
     * 数据操作
     */
    private String action;

    /**
     * 标注时间
     */
    private Date createTime;

    /**
     * 是否删除
     */
    private Boolean destroy;
}
