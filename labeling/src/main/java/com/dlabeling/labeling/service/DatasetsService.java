package com.dlabeling.labeling.service;

import com.dlabeling.labeling.domain.po.Datas;
import com.dlabeling.labeling.domain.po.InterfaceAddress;
import com.dlabeling.labeling.domain.po.Split;
import com.dlabeling.labeling.domain.vo.*;

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

    List<InterfaceAddress> getInterfaceList(Integer datasetID, String type);

    void addDataToSplit(Integer datasetID, Integer splitID, List<Integer> dataIdList);

    List<DatasVO> getDatasByFilter(DatasFilterVO datasFilterVO);

    void batchEditDatas(DatasEditVO datasEditVO);

    List<DatasVO> getSplitDatas(Integer datasetID, Integer splitID);

    DatasVO getDatasByID(Integer datasetID, Integer dataID);

    void updateDatas(DatasVO datasVO);

    void updateDatasetInfo(DatasetsVO datasetsVO);
}
