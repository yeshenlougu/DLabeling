package com.dlabeling.labeling.core.enums;


import lombok.Getter;

import java.util.Arrays;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
@Getter
public enum LabelType {

    Image_Label(1, "ImageLabel"), SQUARE_LABEL(2, "SquareLabel"), POLYGON_LABEL(3, "PolygonLabel"),TEXT_LABEL(4, "TextLabel");
    private int code;
    private String name;

    LabelType(int code, String type) {
        this.code = code;
        this.name = type;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static LabelType getByCode(int code){
        return Arrays.stream(LabelType.values()).filter(labelType -> labelType.getCode() == code).findFirst().orElse(null);
    }

}
