package com.dlabeling.labeling.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dlabeling.labeling.domain.po.Datasets;
import com.dlabeling.labeling.domain.vo.DatasetsVO;
import com.dlabeling.labeling.domain.vo.SetItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
@Mapper
public interface DatasetsMapper extends BaseMapper<Datasets> {

    void addDatasets(Datasets datasets);

    List<Datasets> getAllDataset();

    void updateDatasetsByID(Datasets datasets);

    void deleteDatasetsByID(@Param("id") Integer id);

    Datasets selectByObj(Datasets datasets);

    Datasets selectByID(@Param("id") Integer id);

    List<Datasets> selectDatasetLike(@Param("datasets") Datasets datasets, @Param("creatorList") List<Integer> creatorList);


    List<DatasetsVO> selectDatasetByJoinBatch(@Param("datasetsList") List<Datasets> datasetsList);

    List<DatasetsVO> selectDatasetByJoinBatchWithFilter(@Param("datasetsFilter")DatasetsVO datasets);

    List<String> fetchCreatorList();

    List<Datasets> getDatasetHas(@Param("userId") Integer id);

    List<SetItem> getSetItemList(@Param("type") Integer type);
}
