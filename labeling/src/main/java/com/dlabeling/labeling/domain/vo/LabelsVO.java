package com.dlabeling.labeling.domain.vo;

import com.dlabeling.labeling.core.enums.LabelType;
import lombok.Data;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
@Data
public class LabelsVO {
    private Integer id;

    private Integer datasetId;

    private String labelName;

    private LabelType labelType;
}
