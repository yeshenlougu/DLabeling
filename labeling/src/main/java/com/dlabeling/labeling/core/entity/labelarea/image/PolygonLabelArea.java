package com.dlabeling.labeling.core.entity.labelarea.image;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.Point;
import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
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
