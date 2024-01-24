package com.dlabeling.labeling.domain.vo;

import com.dlabeling.common.utils.DateUtils;
import com.dlabeling.labeling.core.enums.DataBaseType;
import com.dlabeling.labeling.domain.po.Datasets;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
@Data
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
     * 数据集可见性
     */
    private Boolean visible;

    private DataBaseType dataBaseType;

    private List<LabelsVO> labelsVOList;


    public static Datasets convertToDatasets(DatasetsVO datasetsVO){
        Datasets datasets = new Datasets();
        datasets.setName(datasetsVO.getName());
        datasets.setDescription(datasetsVO.getDescription());
        datasets.setType(datasetsVO.getType());
        datasets.setDataRootDir(datasetsVO.getDataRootDir());
        datasets.setVisible(datasetsVO.getVisible());
        datasets.setCreateTime(DateUtils.getNowDate());
        return datasets;
    }
}
