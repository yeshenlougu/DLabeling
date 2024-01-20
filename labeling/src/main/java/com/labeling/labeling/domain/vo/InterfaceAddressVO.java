package com.labeling.labeling.domain.vo;

import com.labeling.labeling.core.enums.InterfaceType;
import com.labeling.labeling.domain.po.InterfaceAddress;

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
    private InterfaceType interfaceType;

    /**
     * 接口别名
     */
    private String interfaceName;

    /**
     * 接口地址
     */
    private String interfaceAddress;


    public static InterfaceAddress convertToInterfaceAddress(){
        return new InterfaceAddress();
    }
}
