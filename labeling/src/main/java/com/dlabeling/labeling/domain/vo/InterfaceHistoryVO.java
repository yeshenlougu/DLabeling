package com.dlabeling.labeling.domain.vo;

import lombok.Data;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/18
 */

@Data
public class InterfaceHistoryVO {

    private Integer id;

    private String name;

    private Integer interfaceId;

    private Integer datasetId;

    private Integer splitId;

    private String interfaceName;

    private String datasetName;

    private String splitName;

}
