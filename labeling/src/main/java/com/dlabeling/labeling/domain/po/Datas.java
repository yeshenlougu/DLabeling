package com.dlabeling.labeling.domain.po;

import lombok.Data;

import java.util.Map;
import java.util.Objects;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/8
 */
@Data
public class Datas {

    private Integer id;

    private String dataPath;

    private String labelPath;

    private Map<String, Objects> labelMap;
}
