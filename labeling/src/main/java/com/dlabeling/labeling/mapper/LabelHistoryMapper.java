package com.dlabeling.labeling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dlabeling.labeling.domain.po.LabelHistory;
import com.dlabeling.labeling.domain.vo.LabelHistoryVO;
import com.dlabeling.labeling.domain.vo.item.LabelHistoryItem;
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
public interface LabelHistoryMapper extends BaseMapper<LabelHistory> {

    void addLabelHistory(LabelHistory labelHistory);


    void deleteLabelHistoryByID(Integer id);

    List<LabelHistory> getLabelHistoryByLabelHistory(LabelHistory labelHistory);

    List<LabelHistoryVO> getAllLabelHistoryVO();

    LabelHistoryVO getLabelHistoryVO(@Param("labelHistoryID") Integer labelHistoryID);
}
