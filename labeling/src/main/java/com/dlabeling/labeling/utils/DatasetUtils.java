package com.dlabeling.labeling.utils;

import com.dlabeling.common.utils.FileUtils;
import com.dlabeling.labeling.common.LabelConstant;
import com.dlabeling.labeling.core.enums.InterfaceType;
import com.dlabeling.labeling.core.enums.LabelType;
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


    public static String labelRootPath(String name, String type, String basePath) {
        int i = name.indexOf("(");
        if (i>0){
            name = name.substring(0,i);
        }
        String path = basePath;
        if (type.equals(InterfaceType.TEST.getName())){
            path = FileUtils.resolvePath(FileUtils.resolvePath(basePath, InterfaceType.TEST.getName()), name);
        }else if (type.equals(InterfaceType.LABEL.getName())){
            path = FileUtils.resolvePath(FileUtils.resolvePath(basePath, InterfaceType.LABEL.getName()), name);
        } else if (type.equals(InterfaceType.CHECK.getName())) {
            path = FileUtils.resolvePath(FileUtils.resolvePath(basePath, InterfaceType.CHECK.getName()), name);
        }
        return path;
    }
}
