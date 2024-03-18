package com.dlabeling.labeling.domain.vo;

import com.dlabeling.labeling.core.enums.InterfaceType;
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

    private Integer interfaceType;


    private String datasetName;

    private String splitName;

    private String type;

    private Integer typeCode;

    public void setType(String type) {
        this.type = type;
        this.typeCode = InterfaceType.getInterfaceTypeByType(type).getCode();
    }

    public void setTypeCode(Integer typeCode) {
        this.typeCode = typeCode;
        this.type = InterfaceType.getInterfaceTypeByCode(typeCode).getName();
    }
}
