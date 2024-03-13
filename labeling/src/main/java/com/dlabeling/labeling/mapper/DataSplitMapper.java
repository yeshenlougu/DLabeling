package com.dlabeling.labeling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dlabeling.labeling.domain.po.DataSplit;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/11
 */
@Mapper
public interface DataSplitMapper extends BaseMapper<DataSplit> {

    void addDataSplit(DataSplit dataSplit);
    void deleteDataSplit(DataSplit dataSplit);

    List<DataSplit> selectDataSplit(DataSplit dataSplit);
}
