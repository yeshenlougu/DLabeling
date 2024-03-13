package com.dlabeling.system.domain.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/11
 */
@Data
public class DatasetPermission implements Serializable {
    private static final long serialVersionUID = 1082660693806197328L;

    private int id;
    private int userId;
    private int datasetId;
}
