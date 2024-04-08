package com.dlabeling.labeling.core.action;

import com.alibaba.fastjson2.JSONObject;
import com.dlabeling.labeling.enums.ActionType;
import lombok.Data;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
@Data
public class Action {

    private Integer actionType;

    private String beforeAction;
    private String afterAction;
}
