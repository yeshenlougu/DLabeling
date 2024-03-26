package com.dlabeling.labeling.domain.vo;

import com.dlabeling.labeling.core.enums.InterfaceType;
import com.dlabeling.labeling.domain.po.InterfaceAddress;
import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
@Data
public class InterfaceAddressVO {

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
    private String interfaceType;

    /**
     * 接口别名
     */
    private String interfaceName;

    /**
     * 接口地址
     */
    private String interfaceAddress;

    private Date destroyTime;

    public void setInterfaceType(Integer interfaceType) {
        this.interfaceType = InterfaceType.getInterfaceTypeByCode(interfaceType).getName();
    }
    public void setInterfaceType(String interfaceType){
        this.interfaceType = interfaceType;
    }

    public static InterfaceAddress convertToInterfaceAddress(){
        return new InterfaceAddress();
    }
}
