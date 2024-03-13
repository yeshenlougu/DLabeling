package com.dlabeling.labeling.domain.po;

import lombok.Data;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/11
 */
@Data
public class Split {
    int id;
    int datasetId;

    int type;

    String name;
}
