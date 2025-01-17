package com.dlabeling.labeling.domain.vo;

import com.dlabeling.labeling.domain.po.Split;
import com.dlabeling.labeling.enums.SplitType;
import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/11
 */
@Data
public class SplitVO {

    private int id;
    private int datasetId;

    private String type;

    private String name;

    private Date createTime;

    private Integer dataCount;

    public void setType(String type) {
        this.type = type;
    }
    public void setType(Integer type){
        this.type = SplitType.getSplitTypeByCode(type).getType();
    }

    public static SplitVO convertToSplitVO(Split split){
        SplitVO splitVO = new SplitVO();
        splitVO.setId(split.getId());
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
