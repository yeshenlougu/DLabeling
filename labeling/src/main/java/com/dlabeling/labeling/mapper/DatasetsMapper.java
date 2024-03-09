package com.dlabeling.labeling.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dlabeling.labeling.domain.po.Datasets;
import org.apache.ibatis.annotations.Mapper;

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

    void deleteDatasetsByID(Integer id);

    Datasets selectByObj(Datasets datasets);

    Datasets selectByID(Integer id);




}
