package com.dlabeling.labeling.domain.vo;

import com.dlabeling.labeling.core.action.Action;
import com.dlabeling.labeling.domain.po.LabelHistory;
import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
@Data
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
     * 用户名称
     */
    private String username;

    /**
     * 数据集id
     */
    private Integer datasetId;

    private String file;

    private String dataPath;

    /**
     * 数据集名称
     */
    private String datasetName;

    /**
     * 被标注数据id
     */
    private Integer dataId;

    /**
     * 数据操作
     */
    private Action action;

    private Date createTime;

    public static LabelHistory convertToLabelHistory(){
        return new LabelHistory();
    }

}
