package com.dlabeling.labeling.enums;

import java.util.stream.Stream;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/25
 */
public enum ActionType {
    VALUE_CHANGE(0, "valueChange", "标注值修改"),
    POS_CHANGE(1, "posChange", "标注坐标修改"),
    ALL_CHANGE(2,"allChange", "全部修改");

    private final int code;
    private final String name;
    private final String description;

    ActionType(int code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static ActionType getSplitTypeByName(String type){
        return Stream.of(ActionType.values())
                .filter(e->e.getName().equals(type))
                .findFirst()
                .orElse(null);
    }

    public static ActionType getSplitTypeByCode(Integer code){
        return Stream.of(ActionType.values())
                .filter(e->e.getCode() == code)
                .findFirst()
                .orElse(null);
    }

    public static ActionType getSplitTypeByDescription(String description){
        return Stream.of(ActionType.values())
                .filter(e->e.getDescription().equals(description))
                .findFirst()
                .orElse(null);
    }
}
