package com.dlabeling.labeling.service;

import com.dlabeling.labeling.domain.vo.DatasVO;
import com.dlabeling.labeling.domain.vo.SetItem;
import com.dlabeling.labeling.domain.vo.SplitVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/25
 */
public interface SplitService {
    List<SplitVO> getSplitVOListByID(Integer datasetId, String type);

    List<DatasVO> getSplitDatas(Integer datasetID, Integer splitID);

    void addDataToSplit(Integer datasetID, Integer splitID, ArrayList<Integer> dataIdList);

    SplitVO addSplit(SplitVO splitVO);

    void deleteSplit(Integer splitID);

    void updateSplit(SplitVO splitVO);

    List<SetItem> getAllSplitByType(String type);

    void deleteData(Integer datasetID, Integer splitID, Integer dataID);
}
