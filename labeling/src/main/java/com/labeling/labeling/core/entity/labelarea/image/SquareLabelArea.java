package com.labeling.labeling.core.entity.labelarea.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SquareLabelArea extends ImageLabelArea {

    private Point leftTop;

    private Point rightTop;

    private Point rightBottom;

    private Point leftBottom;

    @Override
    public List<Point> getLabelArea(){
        List<Point> pointList = new ArrayList<>();
        pointList.add(leftTop);
        pointList.add(rightTop);
        pointList.add(rightBottom);
        pointList.add(leftBottom);

        return pointList;
    }
}
