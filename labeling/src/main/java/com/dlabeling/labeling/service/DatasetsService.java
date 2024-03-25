package com.dlabeling.labeling.service;

import com.dlabeling.labeling.domain.vo.*;
import com.dlabeling.labeling.domain.vo.SetItem;

import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
public interface DatasetsService {

    DatasetsVO getDatasetByID(Integer id);

    List<DatasetsVO> getAllDatasets();

    List<DatasVO> getDatasBySetID(Integer id);

    List<String> getLabelList(Integer datasetId);

    SplitVO addSplit(SplitVO splitVO);

    List<SplitVO> getSplitVOListByID(Integer datasetId, String type);

    List<DatasetsVO> getDatasetByFilter(DatasetsVO datasetsVO);

    List<String> getCreatorList();


    void addDataToSplit(Integer datasetID, Integer splitID, List<Integer> dataIdList);

    List<DatasVO> getDatasByFilter(DatasFilterVO datasFilterVO);

    void batchEditDatas(DatasEditVO datasEditVO);

    List<DatasVO> getSplitDatas(Integer datasetID, Integer splitID);

    DatasVO getDatasByID(Integer datasetID, Integer dataID);

    void updateDatas(DatasVO datasVO);

    void updateDatasetInfo(DatasetsVO datasetsVO);

    List<DatasetsVO> getDatasetHas();

    List<DatasetsVO> getDatasetDontHas();

    List<SetItem> getAllSetByType(String type);
}
