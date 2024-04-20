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
    LABEL(0, "auto", "标注接口"),
    TEST(1, "test", "测试接口"),
    CHECK(2, "check", "查验接口");

    private final int code;
    private final String name;
    private final String description;

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
    
    public static InterfaceType getInterfaceTypeByDescription(String description){
        return Stream.of(InterfaceType.values())
                .filter(interfaceType -> interfaceType.description.equals(description))
                .findFirst()
                .orElse(null);
    }
}
