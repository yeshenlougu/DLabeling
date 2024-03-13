package com.dlabeling.labeling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dlabeling.labeling.domain.po.Split;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/11
 */
@Mapper
public interface SplitMapper extends BaseMapper<Split> {

    void addSplit(Split split);

    List<Split> selectByDatasetId(@Param("datasetId") Integer datasetId, @Param("type") Integer type);

    Split selectSplit(Split split);
}
