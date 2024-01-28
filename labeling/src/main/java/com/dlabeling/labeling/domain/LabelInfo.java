package com.dlabeling.labeling.domain;

import lombok.Data;

import java.util.Map;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/24
 */
@Data
public class LabelInfo {

    private Integer id;

    private String dataPath;

    private String labelPath;

    private Map<String, Object> labels;
}
