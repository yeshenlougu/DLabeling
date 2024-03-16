package com.dlabeling.labeling.domain.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/14
 */

@Data
public class DatasEditVO {
    /**
     * 数据集id
     */
    private int id;

    /**
     * 需要改变的label name
     */
    private List<String> labelList;

    /**
     * 需要改变的 datas id
     */
    private List<Integer> datasList;

    /**
     * 上右下左 变化
     */
    private Map<String, Integer> editForm;
}
