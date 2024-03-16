package com.dlabeling.labeling.service.impl;

import com.alibaba.druid.FastsqlException;
import com.dlabeling.common.exception.file.FileExistsException;
import com.dlabeling.common.utils.FileUtils;
import com.dlabeling.labeling.common.LabelConstant;
import com.dlabeling.labeling.domain.po.Datas;
import com.dlabeling.labeling.domain.po.Datasets;
import com.dlabeling.labeling.mapper.DatasMapper;
import com.dlabeling.labeling.mapper.DatasetsMapper;
import com.dlabeling.labeling.service.UploadService;
import com.dlabeling.labeling.utils.DatasetUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/14
 */
@Slf4j
@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private DatasetsMapper datasetsMapper;

    @Autowired
    private DatasMapper datasMapper;

    @Override
    @Transactional
    public void uploadData(List<MultipartFile> fileList, Integer datasetID) {
        try {
            Datasets datasets = datasetsMapper.selectByID(datasetID);
            String dataRootDir = datasets.getDataRootDir();
            String dataPath = FileUtils.resolvePath(dataRootDir, LabelConstant.DATA_DIR_NAME);
            String labelPath = FileUtils.resolvePath(dataRootDir, LabelConstant.LABEL_DIR_NAME);
            List<String> existFile = new ArrayList<>();
            File desFile = null;
            for (MultipartFile multipartFile : fileList) {
                try {
                    String dataSavePath = FileUtils.resolvePath(dataPath, multipartFile.getOriginalFilename());
                    String labelSavePath = FileUtils.resolvePath(labelPath,
                            FileUtils.changeSuffix(multipartFile.getOriginalFilename(), ".txt"));
                    desFile = new File(dataSavePath);
                    if (desFile.exists()){
                        throw new FileExistsException(multipartFile.getOriginalFilename()+"已存在");
                    }
                    multipartFile.transferTo(desFile);
                    Datas insertDatas = new Datas();
                    insertDatas.setDataPath(dataSavePath);
                    insertDatas.setLabelPath(labelSavePath);

                    String tableName = DatasetUtils.getDataTable(datasetID);
                    datasMapper.insertData(insertDatas, tableName);
                }catch (FileExistsException e){
                    existFile.add(e.getMessage());
                }catch (PersistenceException | FastsqlException e){
                    if (desFile !=null && desFile.exists()){
                        desFile.delete();
                    }
                }

            }
        }catch (Exception e){

        }
    }
}
