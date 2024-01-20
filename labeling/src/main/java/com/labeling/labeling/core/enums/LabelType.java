package com.labeling.labeling.core.enums;


import lombok.Getter;

@Getter
public enum LabelType {

    Image_Label(1, "ImageLabel"), TEXT_LABEL(2, "TextLabel");
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
}
