package com.dlabeling.labeling.domain.vo;

import com.dlabeling.labeling.core.enums.InterfaceType;
import com.dlabeling.labeling.domain.po.Datasets;
import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/18
 */

//@Data
//public class InterfaceHistoryVO {
//
//    private Integer id;
//
//    private String name;
//
//    private Integer datasetId;
//
//    private String datasetName;
//
//    private Integer interfaceId;
//    private String interfaceName;
//
//    private Integer interfaceType;
//
//    private Integer splitId;
//
//    private String splitName;
//
//    private String type;
//
//    private Integer typeCode;
//
//    private Integer dataCount;
//
//    public void setType(String type) {
//        this.type = type;
//        this.typeCode = InterfaceType.getInterfaceTypeByType(type).getCode();
//    }
//
//    public void setTypeCode(Integer typeCode) {
//        this.typeCode = typeCode;
//        this.type = InterfaceType.getInterfaceTypeByCode(typeCode).getName();
//    }
//}
@Data
public class InterfaceHistoryVO {
    private Integer id;
    private String name;
    private String type;
    private Date createTime;

    private InterfaceAddressVO interfaceAddressVO;
    private SplitVO splitVO;
    private Datasets datasets;

}