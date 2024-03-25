package com.dlabeling.labeling.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/24
 */

@Data
public class InterfaceVO {
    private Integer datasetId;
    private String datasetName;

    private List<InterfaceAddressVO> interfaceAddressVOList;
}
