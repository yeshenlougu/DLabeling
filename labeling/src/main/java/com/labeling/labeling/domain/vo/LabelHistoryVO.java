package com.labeling.labeling.domain.vo;

import com.labeling.labeling.core.action.Action;
import com.labeling.labeling.domain.po.LabelHistory;

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
