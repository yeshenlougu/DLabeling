package com.dlabeling.labeling.domain.vo;

import com.dlabeling.labeling.core.action.Action;
import com.dlabeling.labeling.domain.po.LabelHistory;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
public class LabelHistoryVO {

    /**
     * id标识
     */
    private Integer id;

    /**
     * 操作者id
     */
    private Integer userId;

    /**
     * 数据集id
     */
    private Integer datasetId;

    /**
     * 被标注数据id
     */
    private Integer dataId;

    /**
     * 数据操作
     */
    private Action action;

    public static LabelHistory convertToLabelHistory(){
        return new LabelHistory();
    }

}
