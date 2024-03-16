package com.dlabeling.labeling.domain.vo;

import com.alibaba.fastjson2.JSON;
import com.dlabeling.common.utils.DateUtils;
import com.dlabeling.labeling.core.enums.DataBaseType;
import com.dlabeling.labeling.domain.po.Datasets;
import lombok.Data;
import org.apache.ibatis.annotations.Options;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.validation.constraints.NotNull;
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

    private String creator;



    /**
     * 数据集可见性
     */
    private Boolean visible;

    private String dataBaseType;

    private List<LabelConfVO> labelConfList;
    private String labelConfListJson;

    public void setLabelConfListJson(String json){
        this.labelConfListJson = json;
        this.labelConfList = (List<LabelConfVO>) JSON.parse(json);
    }

    public static Datasets convertToDatasets(DatasetsVO datasetsVO){
        Datasets datasets = new Datasets();
        datasets.setName(datasetsVO.getName());
        datasets.setDescription(datasetsVO.getDescription());
        datasets.setType(datasetsVO.getType());
        datasets.setDataRootDir(datasetsVO.getDataRootDir());
        datasets.setVisible(datasetsVO.getVisible());
        return datasets;
    }

    public static DatasetsVO convertToDatasetsVO(Datasets datasets){
        DatasetsVO datasetsVO = new DatasetsVO();
        datasetsVO.setName(datasets.getName());
        datasetsVO.setDescription(datasets.getDescription());
        datasetsVO.setType(datasets.getType());
        datasetsVO.setDataRootDir(datasets.getDataRootDir());
        datasetsVO.setVisible(datasets.getVisible());
        datasetsVO.setId(datasets.getId());
        datasetsVO.setDataBaseType(DataBaseType.getByCode(datasets.getType()).getName());

        return datasetsVO;
    }
}
