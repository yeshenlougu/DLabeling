package com.dlabeling.labeling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dlabeling.labeling.domain.po.LabelConf;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
@Mapper
public interface LabelConfMapper extends BaseMapper<LabelConf> {

    void addLabelConf(LabelConf labelConf);

    void batchAddLabelConf(List<LabelConf> labelConfList);

    List<LabelConf> getLabelConfByDB(Integer datasetId);

    void updateLabelConfByID(LabelConf labelConf);

    void batchUpdateLabelConfByID(List<LabelConf> labelConfList);

    void deleteLabelConfByID(Integer id);

    void deleteLabelConfByDatasetId(Integer id);
}
