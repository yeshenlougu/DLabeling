package com.dlabeling.labeling.domain.po;

import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/18
 */

@Data
public class InterfaceHistory {

    private Integer id;

    private String name;

    private Integer interfaceId;

    private Integer datasetId;

    private Integer splitId;

    private Integer type;

    private Date createTime;
}
