package com.dlabeling.labeling.service.impl;

import com.dlabeling.common.enums.ResponseCode;
import com.dlabeling.common.exception.BusinessException;
import com.dlabeling.common.exception.file.FileNotFileException;
import com.dlabeling.common.utils.FileUtils;
import com.dlabeling.labeling.common.LabelConstant;
import com.dlabeling.labeling.domain.po.Datasets;
import com.dlabeling.labeling.domain.po.LabelConf;
import com.dlabeling.labeling.domain.po.Split;
import com.dlabeling.labeling.domain.vo.DatasVO;
import com.dlabeling.labeling.domain.vo.DatasetsVO;
import com.dlabeling.labeling.domain.vo.LabelConfVO;
import com.dlabeling.labeling.domain.vo.SplitVO;
import com.dlabeling.labeling.enums.SplitType;
import com.dlabeling.labeling.mapper.*;
import com.dlabeling.labeling.service.DatasetsService;
import com.dlabeling.labeling.utils.DatasetUtils;
import com.dlabeling.system.domain.po.user.UserInfo;
import com.dlabeling.system.service.user.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Autowired
    private SplitMapper splitMapper;

    @Autowired
    private DataSplitMapper dataSplitMapper;

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
                datasVO.setFilePath(dataPath);
                datasVO.setFile(base64File);
                datasVO.setLabelPath((String) value.get("label_path"));
                Map<String, String> labelList = new HashMap<>();

                value.remove("id");
                value.remove("data_path");
                value.remove("label_path");

                for (String key : value.keySet()) {
                    String newKey = key;

                    int i = key.lastIndexOf("_");
                    String suffix = key.substring(i+1);
                    String pref = key.substring(0, i);
                    newKey = fieldToLabel.get(pref) + "_" + suffix;

                    labelList.put(newKey, (String)value.get(key));
                }

                datasVO.setLabelList(labelList);

                datasVOList.add(datasVO);
            }catch (IOException | FileNotFileException e){
                continue;
            }

        }

        return datasVOList;
    }


    @Override
    public List<String> getLabelList(Integer datasetId){
        List<LabelConf> labelConfByDB = labelConfMapper.getLabelConfByDB(datasetId);

        List<String> res = labelConfByDB.stream()
                .flatMap(labelConf -> Stream.of(
                        labelConf.getLabelName() + "_" + LabelConstant.DATA_FILED_VALUE,
                        labelConf.getLabelName() + "_" + LabelConstant.DATA_FILED_POSITION,
                        labelConf.getLabelName() + "_" + LabelConstant.DATA_FILED_AUTO,
                        labelConf.getLabelName() + "_" + LabelConstant.DATA_FILED_TEST
                ))
                .collect(Collectors.toList());

        return res;
    }

    @Override
    public SplitVO addSplit(SplitVO splitVO) {
        Split split = SplitVO.convertToSplit(splitVO);
        splitMapper.addSplit(split);
        Split split1 = splitMapper.selectSplit(split);
        return SplitVO.convertToSplitVO(split1);
    }

    @Override
    public List<SplitVO> getSplitVOListByID(Integer datasetId, String type){
        int type2 =  SplitType.getSplitTypeByType(type).getCode();
        List<Split> splits = splitMapper.selectByDatasetId(datasetId, type2);
        List<SplitVO> splitVOList = splits.stream().map(SplitVO::convertToSplitVO).collect(Collectors.toList());
        return splitVOList;
    }

    @Override
    public List<DatasVO> getDatasBySplit(SplitVO splitVO){
        return null;
    }

    @Override
    public List<DatasetsVO> getDatasetByFilter(DatasetsVO datasetsVO) {
        return null;
    }
}
