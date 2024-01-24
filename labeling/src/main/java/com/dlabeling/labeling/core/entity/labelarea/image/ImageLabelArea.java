package com.dlabeling.labeling.core.entity.labelarea.image;

import com.dlabeling.labeling.core.entity.labelarea.BaseLabelArea;

import java.awt.*;
import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
public abstract class ImageLabelArea extends BaseLabelArea {

    public abstract List<Point> getLabelArea();
}
