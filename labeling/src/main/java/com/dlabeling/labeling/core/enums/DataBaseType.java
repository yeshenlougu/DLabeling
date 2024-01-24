package com.dlabeling.labeling.core.enums;

import java.util.Arrays;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
public enum DataBaseType {

    ImageDataBase(1, "ImageDataBase"), TEXTDataBase(2, "TextDataBase");
    private int code;
    private String name;

    DataBaseType(int code, String type) {
        this.code = code;
        this.name = type;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public int getCode(){return code;}

    public String getName() {
        return name;
    }

    public static DataBaseType getByCode(int code){
        return Arrays.stream(DataBaseType.values()).filter(dataBaseType -> dataBaseType.getCode() == code).findFirst().orElse(null);
    }
}
