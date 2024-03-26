package com.dlabeling.labeling.domain.po;

import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/11
 */
@Data
public class Split {
    private int id;
    private int datasetId;

    private int type;

    private String name;

    private Date createTime;
}
