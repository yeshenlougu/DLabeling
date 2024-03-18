package com.dlabeling.labeling.utils;

import com.dlabeling.labeling.common.LabelConstant;
import com.dlabeling.labeling.domain.po.Datas;
import com.dlabeling.labeling.domain.vo.DatasVO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/8
 */
public class DatasetUtils {

    public static String getDataTable(Integer datasetId){
        return LabelConstant.TABLE_GEN_PREF + "_" + datasetId;
    }

    public static String getFieldLabel(int labelId){return LabelConstant.DATA_FILED_PREF+"_"+labelId;}


}
