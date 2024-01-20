package com.labeling.labeling.core.entity.labelarea.image;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.Point;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
public class PolygonLabelArea extends ImageLabelArea{

    private List<Point> pointList;

    public void addPoint(Point point){
        pointList.add(point);
    }

    public void removePoint(Point point){
        pointList.remove(point);
    }

    @Override
    public List<Point> getLabelArea() {
        return null;
    }
}
