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