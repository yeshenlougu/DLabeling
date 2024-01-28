package com.dlabeling.labeling.domain.vo;

import com.dlabeling.labeling.core.enums.LabelType;
import com.dlabeling.labeling.domain.po.LabelConf;
import lombok.Data;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
@Data
public class LabelConfVO {
    private Integer id;

    private Integer datasetId;

    private String labelName;

    private LabelType labelType;

    public static LabelConf convertToLabelConf(LabelConfVO labelConfVO){
        LabelConf labelConf = new LabelConf();
        labelConf.setLabelName(labelConfVO.getLabelName());
        labelConf.setLabelType(labelConf.getLabelType());
        labelConf.setDatasetId(labelConf.getDatasetId());
        labelConf.setId(labelConfVO.getId());

        return labelConf;
    }

    public static LabelConfVO convertToLabelConfVO(LabelConf labelConf){
        LabelConfVO labelConfVO = new LabelConfVO();
        labelConfVO.setId(labelConf.getId());
        labelConfVO.setDatasetId(labelConf.getDatasetId());
        labelConfVO.setLabelName(labelConf.getLabelName());
        labelConfVO.setLabelType(LabelType.getByCode(labelConf.getLabelType()));
        return labelConfVO;
    }
}
