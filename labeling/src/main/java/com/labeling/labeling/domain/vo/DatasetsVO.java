package com.labeling.labeling.domain.vo;

import com.labeling.labeling.domain.po.Datasets;

public class DatasetsVO {

    /**
     * 数据集编号
     */
    private Integer id;

    /**
     * 数据集名称
     */
    private String name;

    /**
     * 数据集描述
     */
    private String description;

    /**
     * 数据集类型
     */
    private Integer type;

    /**
     * 数据存储目录
     */
    private String dataRootDir;

    /**
     * 标注存储目录
     */
    private String labelRootDir;

    /**
     * 数据集可见性
     */
    private Boolean visible;


    public static Datasets convertToDatasets(){
        return new Datasets();
    }
}
