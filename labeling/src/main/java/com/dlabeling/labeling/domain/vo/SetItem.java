package com.dlabeling.labeling.domain.vo;

import com.dlabeling.labeling.domain.po.LabelConf;
import com.dlabeling.labeling.domain.vo.InterfaceHistoryVO;
import com.dlabeling.labeling.domain.vo.SplitVO;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/24
 */

@Data
public class SetItem {
    private String datasetId;
    private String datasetName;
    private List<SplitVO> splitVOList;

    private List<InterfaceHistoryVO> interfaceHistoryVOList;

}
