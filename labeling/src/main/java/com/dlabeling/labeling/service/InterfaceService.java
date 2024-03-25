package com.dlabeling.labeling.service;

import com.dlabeling.labeling.domain.po.InterfaceAddress;
import com.dlabeling.labeling.domain.po.InterfaceHistory;
import com.dlabeling.labeling.domain.vo.DatasVO;
import com.dlabeling.labeling.domain.vo.DoLabelVO;
import com.dlabeling.labeling.domain.vo.InterfaceHistoryVO;
import com.dlabeling.labeling.domain.vo.InterfaceVO;

import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/18
 */
public interface InterfaceService {

    void addInterfaceHistory(InterfaceHistory  interfaceHistory);

    List<InterfaceHistoryVO> getInterfaceHistoryList(Integer datasetID, String type);

    void doLabelInterface(DoLabelVO doLabelVO);

    List<DatasVO> getLabelHistoryDatasList(InterfaceHistoryVO interfaceHistory);

    List<InterfaceAddress> getInterfaceList(Integer datasetID, String type);

    List<InterfaceVO> getAllInterface();
}
