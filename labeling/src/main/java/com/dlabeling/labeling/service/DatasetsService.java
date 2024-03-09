package com.dlabeling.labeling.service;

import com.dlabeling.labeling.domain.po.Datas;
import com.dlabeling.labeling.domain.vo.DatasVO;
import com.dlabeling.labeling.domain.vo.DatasetsVO;

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

    List<DatasVO> getDatasBySetID(Integer id, Integer start, Integer end);
}
