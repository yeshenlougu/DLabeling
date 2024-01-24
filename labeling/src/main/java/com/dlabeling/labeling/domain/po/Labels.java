package com.dlabeling.labeling.domain.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
@Data
public class Labels implements Serializable {

    private Integer id;

    private Integer datasetId;

    private String labelName;

    private Integer labelType;
}
