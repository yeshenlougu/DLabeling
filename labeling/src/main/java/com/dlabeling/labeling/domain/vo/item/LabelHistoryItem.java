package com.dlabeling.labeling.domain.vo.item;

import com.dlabeling.labeling.domain.vo.InterfaceHistoryVO;
import com.dlabeling.labeling.domain.vo.LabelHistoryVO;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/24
 */

@Data
public class LabelHistoryItem {

    private String datasetId;
    private String datasetName;
    private List<InterfaceHistoryVO> interfaceHistoryVOList;
}
