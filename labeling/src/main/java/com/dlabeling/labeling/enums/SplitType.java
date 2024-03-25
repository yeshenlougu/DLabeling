package com.dlabeling.labeling.enums;

import com.dlabeling.common.enums.UserRole;

import java.util.stream.Stream;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/11
 */
public enum SplitType {
    TRAIN(0, "train", "训练集"), TEST(1, "test", "测试集");
    private int code;
    private String type;
    private String description;

    SplitType(int code, String type, String description){
        this.code = code;
        this.type = type;
        this.description =description;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public String getDescription(){
        return description;
    }

    public static SplitType getSplitTypeByType(String type){
        return Stream.of(SplitType.values())
                .filter(e->e.type.equals(type))
                .findFirst()
                .orElse(null);
    }

    public static SplitType getSplitTypeByCode(Integer code){
        return Stream.of(SplitType.values())
                .filter(e->e.code == code)
                .findFirst()
                .orElse(null);
    }
}
