package com.dlabeling.system.enums;


import java.util.stream.Stream;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/11
 */
public enum LevelApplyType {

    USER_APPLY(0, "用户权限申请"), DATASET_APPLY(1, "数据集权限申请");
    int code;
    String type;

    LevelApplyType(int code, String type){
        this.code = code;
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public static LevelApplyType getLevelApplyTypeByType(String type){
        return Stream.of(LevelApplyType.values())
                .filter(e->e.type.equals( type))
                .findFirst()
                .orElse(null);
    }

    public static LevelApplyType getLevelApplyTypeByCode(Integer code){
        return Stream.of(LevelApplyType.values())
                .filter(e->e.code == code)
                .findFirst()
                .orElse(null);
    }

}
