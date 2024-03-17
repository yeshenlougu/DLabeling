package com.dlabeling.labeling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dlabeling.labeling.domain.po.Datas;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/8
 */
@Mapper
public interface DatasMapper extends BaseMapper<Datas> {


//    @MapKey("id")
//    Map<String, Map<String, Object>> selectDataLimit(@Param("limit") Integer limit, @Param("offset") Integer offset, @Param("table") String table);

    @MapKey("id")
    Map<String, Map<String, Object>> selectDataLimit( @Param("table") String table);

    void insertData(@Param("insertDatas") Datas insertDatas, @Param("table") String table);

//    @MapKey("id")
//    Map<String, Map<String, Object>> selectDataFilterLimit(@Param("labelValueMap") Map<String, String> labelValueMap, @Param("table") String table, @Param("offset") Integer offset, @Param("limit") Integer limit);

    @MapKey("id")
    Map<String, Map<String, Object>> selectDataFilterLimit(@Param("labelValueMap") Map<String, String> labelValueMap, @Param("table") String table);

    @MapKey("id")
    Map<String, Map<String, Object>> updateDatasBatch(@Param("table") String table, @Param("labelValeMap") Map<String, String> labelValueMap);

    @MapKey("id")
    Map<String, Map<String, Object>> selectDatasByIDList(@Param("table") String table, @Param("editDatasList") List<Integer> editDatasList);

    void updateDatas(@Param("datas") Datas datas, @Param("table") String table);

    @MapKey("id")
    Map<String, Map<String, Object>> selectDatasBySplit(@Param("table") String table,@Param("datasetID") Integer datasetID,@Param("splitID") Integer splitID);

    @MapKey("id")
    Map<String, Map<String, Object>> selectDatasByID(@Param("table") String table, @Param("id") Integer dataID);
}
