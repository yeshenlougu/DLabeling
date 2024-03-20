package com.dlabeling.common.enums;

import java.util.stream.Stream;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/5
 */
public enum Gender {
    UNKNOWN(0, "无"), FEMALE(1, "女"), MALE(2, "男");

    private int code;

    private String sex;


    Gender(int code, String role) {
        this.code = code;
        this.sex = role;

    }

    public int getCode() {
        return code;
    }

    public String getSex() {
        return sex;
    }

    public static Gender getGenderByCode(int code){
        return Stream.of(Gender.values())
                .filter(e->e.code == code)
                .findFirst()
                .orElse(null);
    }

    public static Gender getGenderByString(String msg){
        return Stream.of(Gender.values())
                .filter(e->e.sex.equals(msg))
                .findFirst()
                .orElse(null);
    }
}
