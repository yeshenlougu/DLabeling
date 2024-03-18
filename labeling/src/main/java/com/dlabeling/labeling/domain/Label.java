package com.dlabeling.labeling.domain;

import lombok.Data;

import java.awt.*;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/18
 */
@Data
public class Label {

    private Integer id;

    private String type;

    private String value;

    private String name;

    private Rectangle rectangle;
}
