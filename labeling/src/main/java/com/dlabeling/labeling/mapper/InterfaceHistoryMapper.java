package com.dlabeling.labeling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dlabeling.labeling.domain.po.InterfaceHistory;
import com.dlabeling.labeling.domain.vo.InterfaceHistoryVO;
import com.dlabeling.labeling.domain.vo.item.LabelHistoryItem;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/18
 */
@Mapper
public interface InterfaceHistoryMapper extends BaseMapper<InterfaceHistory> {

    void addInterfaceHistory(InterfaceHistory interfaceHistory);

    InterfaceHistoryVO selectInterfaceHistoryVO(InterfaceHistory interfaceHistory);

    List<InterfaceHistoryVO> selectAllInterfaceHistory();

    List<InterfaceHistoryVO> selectInterfaceHistoryBySplitType(@Param("datasetID") Integer datasetID, @Param("type") Integer type);

    @MapKey("id")
    Map<String, Map<String, Object>> selectDatasOfInterfaceHistory(@Param("interfaceHistoryId") Integer interfaceHistoryId, @Param("table") String table);

    List<LabelHistoryItem> getAllLabelHistoryItem();
}
