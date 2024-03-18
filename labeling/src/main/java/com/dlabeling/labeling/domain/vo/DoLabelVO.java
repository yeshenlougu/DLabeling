package com.dlabeling.labeling.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/18
 */
@Data
public class DoLabelVO {

    private String labelType; // test auto check
    private Integer datasetId;
    private Integer interfaceId;
    private Integer splitId;
    private List<Integer> datasIdList;
    private String name;

}
