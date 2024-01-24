package com.dlabeling.labeling.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
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
}
