package com.dlabeling.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dlabeling.system.domain.po.DatasetPermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/11
 */
@Mapper
public interface DatasetPermissionMapper extends BaseMapper<DatasetPermission> {

    void addDatasetPermission(DatasetPermission datasetPermission);

    void deleteDatasetPermission(DatasetPermission datasetPermission);

    DatasetPermission selectDatasetPermission(DatasetPermission datasetPermission);

    List<DatasetPermission> selectDatasetPermissionById(Integer datasetId);
}
