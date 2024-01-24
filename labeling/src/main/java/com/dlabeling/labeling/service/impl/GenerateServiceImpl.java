package com.dlabeling.labeling.service.impl;

import com.dlabeling.common.exception.file.FileException;
import com.dlabeling.common.utils.FileUtils;
import com.dlabeling.labeling.common.LabelConstant;
import com.dlabeling.labeling.core.enums.DataBaseType;
import com.dlabeling.labeling.domain.po.Datasets;
import com.dlabeling.labeling.domain.vo.DatasetsVO;
import com.dlabeling.labeling.domain.vo.LabelsVO;
import com.dlabeling.labeling.mapper.DatasetsMapper;
import com.dlabeling.labeling.service.DatasetsService;
import com.dlabeling.labeling.service.GenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
@Service
public class GenerateServiceImpl implements GenerateService {

    @Autowired
    DatasetsMapper datasetsMapper;

    @Override
    @Transactional
    public void makeDataBase(DatasetsVO datasetsVO) throws FileException{
        Datasets datasets = DatasetsVO.convertToDatasets(datasetsVO);
        datasetsMapper.addDatasets(datasets);
        Datasets selectDatasets = datasetsMapper.selectByObj(datasets);
        String dbRootDir = datasetsVO.getDataRootDir();

        // 创建数据集存储目录
        createDataBaseDir(dbRootDir);

        // 生成Label文件
        createLabelTxt(dbRootDir, datasetsVO.getLabelsVOList());



    }

    /**
     * 创建数据集目录
     * @param rootPath
     */
    private void createDataBaseDir(String rootPath){
        FileUtils.makeDir(rootPath);
        FileUtils.makeDir(FileUtils.resolvePath(rootPath, LabelConstant.DATA_DIR_NAME));
        FileUtils.makeDir(FileUtils.resolvePath(rootPath, LabelConstant.LABEL_DIR_NAME));
        FileUtils.makeDir(FileUtils.resolvePath(rootPath, LabelConstant.AUTO_DIR_NAME));
        FileUtils.makeDir(FileUtils.resolvePath(rootPath, LabelConstant.TEST_DIR_NAME));
        FileUtils.makeDir(FileUtils.resolvePath(rootPath, LabelConstant.CHECK_DIR_NAME));
    }

    private void createLabelTxt(String rootPath, List<LabelsVO> labelsVOS){
        String filePath = FileUtils.resolvePath(rootPath, LabelConstant.LABEL_TXT_FILE);
        FileUtils.makeFile(filePath);

        String content = labelsVOS.stream()
                .map(LabelsVO::getLabelName)
                .collect(Collectors.joining("\n"));
        FileUtils.writeString(filePath, content);
    }

    private void createDBTable(Datasets datasets){
        DataBaseType dbType = DataBaseType.getByCode(datasets.getType());
        Integer dataBaseId = datasets.getId();

    }

    private Boolean isAbsoluteDir(String path){
        return Boolean.TRUE;
    }

    @Override
    public void makeLabelFile(File file) {

    }


}
