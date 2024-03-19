package com.dlabeling.labeling.domain.vo;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
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

    private Integer labelType;

    public static LabelConf convertToLabelConf(LabelConfVO labelConfVO){
        LabelConf labelConf = new LabelConf();
        labelConf.setLabelName(labelConfVO.getLabelName());
        labelConf.setLabelType(labelConfVO.getLabelType());
        labelConf.setDatasetId(labelConfVO.getDatasetId());
        labelConf.setId(labelConfVO.getId());

        return labelConf;
    }

    public static LabelConfVO convertToLabelConfVO(LabelConf labelConf){
        LabelConfVO labelConfVO = new LabelConfVO();
        labelConfVO.setId(labelConf.getId());
        labelConfVO.setDatasetId(labelConf.getDatasetId());
        labelConfVO.setLabelName(labelConf.getLabelName());
        labelConfVO.setLabelType(labelConf.getLabelType());
        return labelConfVO;
    }

    public static LabelConfVO convertJsonObjectToLabelConfVO(JSONObject labelConfVO){

        LabelConfVO labelConf = new LabelConfVO();
        labelConf.setLabelName((String) labelConfVO.get("labelName"));
        labelConf.setLabelType((Integer) labelConfVO.get("labelType"));
        labelConf.setDatasetId((Integer) labelConfVO.get("datasetId"));
        labelConf.setId((Integer) labelConfVO.get("id"));

        return labelConf;
    }
}
