package com.dlabeling.labeling.service;

import com.dlabeling.labeling.domain.po.InterfaceAddress;
import com.dlabeling.labeling.domain.po.InterfaceHistory;
import com.dlabeling.labeling.domain.vo.*;
import com.dlabeling.labeling.domain.vo.item.LabelHistoryItem;

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

    DatasVO getLabelHistoryDatas(Integer interfaceHistoryID, Integer datasetID, String interfaceHistoryName, String type, Integer dataID);

    List<InterfaceAddressVO> getInterfaceList(Integer datasetID, String type);

    List<InterfaceVO> getAllInterface();

    List<LabelHistoryItem> getAllLabelHistoryVO(String type);

    void updateInterfaceAddress(InterfaceAddress interfaceAddress);

    void deleteInterfaceAddress(Integer id);
}
