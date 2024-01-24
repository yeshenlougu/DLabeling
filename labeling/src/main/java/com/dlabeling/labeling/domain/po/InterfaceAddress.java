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
public class InterfaceAddress implements Serializable {
    private static final long serialVersionUID = 3818513207880491071L;

    /**
     * id标识
     */
    private Integer id;

    /**
     * 数据集标识符
     */
    private Integer datasetId;

    /**
     * DATASET名称
     */
    private String datasetName;

    /**
     * 接口所属类型，(0:自动标注,1:测试,2:查验)
     */
    private Integer interfaceType;

    /**
     * 接口别名
     */
    private String interfaceName;

    /**
     * 接口地址
     */
    private String interfaceAddress;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 删除时间
     */
    private Date destroyTime;
}
