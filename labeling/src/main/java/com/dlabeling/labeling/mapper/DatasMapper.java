package com.dlabeling.labeling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dlabeling.labeling.domain.po.Datas;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/8
 */
@Mapper
public interface DatasMapper extends BaseMapper<Datas> {


    @MapKey("id")
    Map<String, Map<String, Object>> selectDataLimit(@Param("limit") Integer limit, @Param("offset") Integer offset, @Param("table") String table);
}
