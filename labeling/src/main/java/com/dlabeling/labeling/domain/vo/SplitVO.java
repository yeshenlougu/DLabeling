package com.dlabeling.labeling.domain.vo;

import com.dlabeling.labeling.domain.po.Split;
import com.dlabeling.labeling.enums.SplitType;
import lombok.Data;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/11
 */
@Data
public class SplitVO {

    int id;
    int datasetId;

    String type;

    String name;

    public static SplitVO convertToSplitVO(Split split){
        SplitVO splitVO = new SplitVO();
        splitVO.setId(splitVO.getId());
        splitVO.setDatasetId(split.getDatasetId());
        splitVO.setName(split.getName());
        splitVO.setType(SplitType.getSplitTypeByCode(split.getType()).getType());

        return splitVO;
    }

    public static Split convertToSplit(SplitVO splitVO){
        Split split = new Split();
        split.setId(splitVO.getId());
        split.setDatasetId(splitVO.getDatasetId());
        split.setName(splitVO.getName());
        split.setType(SplitType.getSplitTypeByType(splitVO.getType()).getCode());

        return split;
    }
}
