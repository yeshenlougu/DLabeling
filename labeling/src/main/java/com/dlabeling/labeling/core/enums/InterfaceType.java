package com.dlabeling.labeling.core.enums;

import com.dlabeling.labeling.enums.SplitType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
@Getter
@AllArgsConstructor
public enum InterfaceType {
    LABEL(0, "label"), TEST(1, "test"), CHECK(2, "check");
    private int code;

    private String name;

    public static InterfaceType getInterfaceTypeByType(String type) {
        return Stream.of(InterfaceType.values())
                .filter(interfaceType -> interfaceType.name.equals(type))
                .findFirst()
                .orElse(null);
    }

    public static InterfaceType getInterfaceTypeByCode(Integer code) {
        return Stream.of(InterfaceType.values())
                .filter(interfaceType -> interfaceType.code==code)
                .findFirst()
                .orElse(null);
    }
}
