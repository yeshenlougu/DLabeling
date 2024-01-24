package com.dlabeling.labeling.service;

import com.dlabeling.labeling.domain.po.Datasets;
import com.dlabeling.labeling.domain.vo.DatasetsVO;

import java.io.File;
/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
public interface GenerateService {


    public void makeDataBase(DatasetsVO datasetsVO);

    public void makeLabelFile(File file);
}
