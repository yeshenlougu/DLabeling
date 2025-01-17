package com.dlabeling.labeling.service;

import com.dlabeling.labeling.domain.po.LabelHistory;
import com.dlabeling.labeling.domain.vo.DatasVO;
import com.dlabeling.labeling.domain.vo.LabelHistoryVO;
import com.dlabeling.labeling.domain.vo.item.LabelHistoryItem;

import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/24
 */
public interface LabelService {

    List<LabelHistoryVO> getAllLabelHistoryVO();

    void addLabelHistory(LabelHistory labelHistory);

    LabelHistory judgeDifference(DatasVO before, DatasVO after);

    LabelHistoryVO getLabelHistoryVO(Integer datasetID, Integer labelHistoryID);
}
