package com.dlabeling.labeling.service.impl;

import com.dlabeling.common.enums.ResponseCode;
import com.dlabeling.common.exception.BusinessException;
import com.dlabeling.common.exception.file.FileNotFileException;
import com.dlabeling.common.utils.FileUtils;
import com.dlabeling.common.utils.StringUtils;
import com.dlabeling.labeling.common.LabelConstant;
import com.dlabeling.labeling.domain.po.Datasets;
import com.dlabeling.labeling.domain.po.LabelConf;
import com.dlabeling.labeling.domain.po.Datas;
import com.dlabeling.labeling.domain.vo.DatasVO;
import com.dlabeling.labeling.domain.vo.DatasetsVO;
import com.dlabeling.labeling.domain.vo.LabelConfVO;
import com.dlabeling.labeling.mapper.DatasMapper;
import com.dlabeling.labeling.mapper.DatasetsMapper;
import com.dlabeling.labeling.mapper.LabelConfMapper;
import com.dlabeling.labeling.service.DatasetsService;
import com.dlabeling.labeling.utils.DatasetUtils;
import com.dlabeling.system.domain.po.user.UserInfo;
import com.dlabeling.system.service.user.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
@Service
@Slf4j
public class DatasetsServiceImpl implements DatasetsService {

    @Autowired
    private DatasetsMapper datasetsMapper;

    @Autowired
    private LabelConfMapper labelConfMapper;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private DatasMapper datasMapper;

    @Override
    public DatasetsVO getDatasetByID(Integer id) {
        try {
            Datasets datasets = datasetsMapper.selectByID(id);
            List<LabelConf> labelConfByDB = labelConfMapper.getLabelConfByDB(datasets.getId());
            DatasetsVO datasetsVO = DatasetsVO.convertToDatasetsVO(datasets);

            List<LabelConfVO> labelConfVOList = labelConfByDB.stream()
                    .map(LabelConfVO::convertToLabelConfVO)
                    .collect(Collectors.toList());
            datasetsVO.setLabelConfList(labelConfVOList);

            UserInfo userInfoById = userService.getUserInfoById(datasets.getCreator());
            datasetsVO.setCreator(userInfoById.getUsername());

            return datasetsVO;
        }catch (Exception e){
            throw new BusinessException(ResponseCode.BUSINESS_ERROR, "获取数据集信息失败");
        }
    }


    @Override
    public List<DatasetsVO> getAllDatasets() {
        try {
            List<Datasets> datasetsList = datasetsMapper.getAllDataset();
            List<DatasetsVO> datasetsVOList = datasetsList.stream()
                    .map(datasets -> {
                        DatasetsVO datasetsVO = DatasetsVO.convertToDatasetsVO(datasets);

                        List<LabelConf> labelConfList = labelConfMapper.getLabelConfByDB(datasetsVO.getId());
                        List<LabelConfVO> labelConfVOList = labelConfList.stream()
                                .map(LabelConfVO::convertToLabelConfVO)
                                .collect(Collectors.toList());
                        datasetsVO.setLabelConfList(labelConfVOList);

                        UserInfo userInfoById = userService.getUserInfoById(datasets.getCreator());
                        datasetsVO.setCreator(userInfoById.getUsername());

                        return datasetsVO;
                    })
                    .collect(Collectors.toList());


            return datasetsVOList;
        }catch (Exception e){
            throw new BusinessException(ResponseCode.BUSINESS_ERROR, "获取所有数据集");
        }
    }

    @Override
    public List<DatasVO> getDatasBySetID(Integer id, Integer start, Integer end){
        List<LabelConf> labelConfByDB = labelConfMapper.getLabelConfByDB(id);
        String tableName = DatasetUtils.getDataTable(id);
        Integer limit = end - start;
        Map<String, String> fieldToLabel = new HashMap<>();

        labelConfByDB.forEach(labelConf -> {
            String field = LabelConstant.DATA_FILED_PREF + "_" + labelConf.getId();
            fieldToLabel.put(field, labelConf.getLabelName());
        });


        Map<String, Map<String, Object>> stringObjectMap = datasMapper.selectDataLimit(limit, start, tableName);

        List<DatasVO> datasVOList = new ArrayList<>();

        for (Map<String, Object> value : stringObjectMap.values()) {
            try {
                String dataPath = (String) value.get("data_path");
                String base64File = FileUtils.getBase64(dataPath);
                DatasVO datasVO = new DatasVO();

                datasVO.setId((Integer) value.get("id"));
                datasVO.setFileName(FileUtils.removeFileExtension(FileUtils.getFileName(dataPath)));

                datasVO.setFile(base64File);
                Map<String, Object> labelList = new HashMap<>();
                for (String key : value.keySet()) {
                    String newKey = key;
                    if (!(key.equals("id") || key.equals("data_path") || key.equals("label_path"))){
                        int i = key.lastIndexOf("_");
                        String suffix = key.substring(i+1);
                        String pref = key.substring(0, i);
                        newKey = fieldToLabel.get(pref) + "_" + suffix;
                    }
                    labelList.put(newKey, value.get(key));
                }

                datasVO.setLabelList(labelList);

                datasVOList.add(datasVO);
            }catch (IOException | FileNotFileException e){
                continue;
            }

        }

        return datasVOList;
    }


}
