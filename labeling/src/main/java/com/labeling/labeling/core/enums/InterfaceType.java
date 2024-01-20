package com.labeling.labeling.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InterfaceType {
    LABEL(0, "label"), TEST(1, "test"), CHECK(2, "check");
    private int code;

    private String name;
}
