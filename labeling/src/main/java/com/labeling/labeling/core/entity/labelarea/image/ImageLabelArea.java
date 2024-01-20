package com.labeling.labeling.core.entity.labelarea.image;

import com.labeling.labeling.core.entity.labelarea.BaseLabelArea;

import java.awt.*;
import java.util.List;


public abstract class ImageLabelArea extends BaseLabelArea {

    public abstract List<Point> getLabelArea();
}
