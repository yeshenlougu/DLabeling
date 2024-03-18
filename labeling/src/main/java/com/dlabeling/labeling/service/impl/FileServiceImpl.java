package com.dlabeling.labeling.service.impl;

import com.alibaba.druid.FastsqlException;
import com.dlabeling.common.config.DLabelingConfig;
import com.dlabeling.common.exception.file.FileExistsException;
import com.dlabeling.common.exception.file.FileMoveException;
import com.dlabeling.common.utils.FileUtils;
import com.dlabeling.common.utils.ZipUtils;
import com.dlabeling.labeling.common.LabelConstant;
import com.dlabeling.labeling.config.LabelConfig;
import com.dlabeling.labeling.domain.po.Datas;
import com.dlabeling.labeling.domain.po.Datasets;
import com.dlabeling.labeling.domain.po.LabelConf;
import com.dlabeling.labeling.domain.vo.DatasVO;
import com.dlabeling.labeling.domain.vo.SplitVO;
import com.dlabeling.labeling.mapper.DatasMapper;
import com.dlabeling.labeling.mapper.DatasetsMapper;
import com.dlabeling.labeling.mapper.LabelConfMapper;
import com.dlabeling.labeling.service.FileService;
import com.dlabeling.labeling.utils.DatasetUtils;
import com.dlabeling.labeling.utils.LabelWriteUtils;
import com.dlabeling.plugin.constant.LabelFileType;
import com.dlabeling.plugin.serivce.TransformService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/14
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private DatasetsMapper datasetsMapper;

    @Autowired
    private DatasMapper datasMapper;

    @Autowired
    private TransformService transformService;

    @Autowired
    private LabelConfMapper labelConfMapper;

    @Autowired
    private LabelConfig labelConfig;

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
                            FileUtils.changeSuffix(multipartFile.getOriginalFilename(), ".json"));
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

                    List<LabelConf> labelConfByDB = labelConfMapper.getLabelConfByDB(datasetID);
                    Map<String, String> filed2label = new HashMap<>();
                    Map<String, Object> datasVOMap = new HashMap<>();
                    List<String> labelNameList = new ArrayList<>();
                    datasVOMap.put("data_path", dataSavePath);
                    datasVOMap.put("label_path", labelSavePath);
                    labelConfByDB.stream().forEach(label->{
                        datasVOMap.put("label_"+label.getId()+"_value", "");
                        datasVOMap.put("label_"+label.getId()+"_pos", "");
                        datasVOMap.put("label_"+label.getId()+"_auto", "");
                        datasVOMap.put("label_"+label.getId()+"_test", "");
                        filed2label.put("label_"+label.getId(), label.getLabelName());
                        labelNameList.add(label.getLabelName());
                    });
                    DatasVO datasVO = DatasVO.convertMapToDatasVO(datasVOMap, filed2label);
                    LabelWriteUtils.writeLabelJSON(labelSavePath, datasVO, labelNameList);


                }catch (FileExistsException e){
                    existFile.add(e.getMessage());
                }catch (PersistenceException | FastsqlException e){
                    if (desFile !=null && desFile.exists()){
                        desFile.delete();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void exportLabel(SplitVO splitVO, HttpServletResponse response) {
        String table = DatasetUtils.getDataTable(splitVO.getDatasetId());
        Map<String, Map<String, Object>> stringMapMap = datasMapper.selectDatasBySplit(table, splitVO.getDatasetId(), splitVO.getId());

        String defaultDataRootDir = labelConfig.getDefaultDataRootDir();
        String tempNewDir = FileUtils.resolvePath(defaultDataRootDir, table);
        FileUtils.makeDir(FileUtils.resolvePath(tempNewDir, "data"));
        FileUtils.makeDir(FileUtils.resolvePath(tempNewDir, "label"));

        /**
         * stringMapMap  {id: {data_path, label_path}}
         */
        for (Map.Entry<String, Map<String, Object>> stringMapEntry : stringMapMap.entrySet()) {
            try {
                Map<String, Object> entryValue = stringMapEntry.getValue();
                String dataPath = (String) entryValue.get("data_path");
                String labelPath = (String) entryValue.get("label_path");
                File baseLabelFile = new File(labelPath);
//                File labelFile = transformService.transform(LabelFileType.JSON_IMAGE, LabelFileType.SIMPLE_DATASET_IMAGE, baseLabelFile);
                String newLabelPath = FileUtils.resolvePath(tempNewDir, FileUtils.resolvePath("label", FileUtils.getFileName(labelPath)));
                FileUtils.copyTo(labelPath, newLabelPath);
//                FileUtils.replace(labelFile, FileUtils.resolvePath(tempNewDir, "label"));

                String newDataPath = FileUtils.resolvePath(tempNewDir, FileUtils.resolvePath("data", FileUtils.getFileName(dataPath)));
                FileUtils.copyTo(dataPath, newDataPath);

            }catch (FileMoveException e){

            }

        }
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(new File("C:\\Users\\YYS\\Desktop\\1.zip"));
            ZipUtils.toZip(tempNewDir, fileOutputStream, true);
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=datas.zip");

            ZipUtils.toZip(tempNewDir, response.getOutputStream(), true);


        }catch (IOException e){

        }finally {
            FileUtils.deleteDir(tempNewDir);
        }



    }
}
