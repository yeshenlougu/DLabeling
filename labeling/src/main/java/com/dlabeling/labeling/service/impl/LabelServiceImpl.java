package com.dlabeling.labeling.service.impl;

import com.alibaba.fastjson2.JSON;
import com.dlabeling.common.enums.ResponseCode;
import com.dlabeling.common.exception.BusinessException;
import com.dlabeling.common.utils.FileUtils;
import com.dlabeling.common.utils.StringUtils;
import com.dlabeling.labeling.domain.json.Pos;
import com.dlabeling.labeling.domain.json.Position;
import com.dlabeling.labeling.domain.po.LabelHistory;
import com.dlabeling.labeling.domain.vo.DatasVO;
import com.dlabeling.labeling.domain.vo.LabelHistoryVO;
import com.dlabeling.labeling.enums.ActionType;
import com.dlabeling.labeling.mapper.DatasMapper;
import com.dlabeling.labeling.mapper.InterfaceHistoryMapper;
import com.dlabeling.labeling.mapper.LabelHistoryMapper;
import com.dlabeling.labeling.service.LabelService;
import com.dlabeling.labeling.utils.DatasetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/24
 */
@Service
public class LabelServiceImpl implements LabelService {

    @Autowired
    private LabelHistoryMapper labelHistoryMapper;

    @Autowired
    private InterfaceHistoryMapper interfaceHistoryMapper;

    @Autowired
    private DatasMapper datasMapper;

    @Override
    public List<LabelHistoryVO> getAllLabelHistoryVO() {
        List<LabelHistoryVO> labelHistoryVOList = labelHistoryMapper.getAllLabelHistoryVO();
        labelHistoryVOList.forEach(labelHistoryVO -> {
            try {
                String table = DatasetUtils.getDataTable(labelHistoryVO.getDatasetId());
                Map<String, Map<String, Object>> stringMapMap = datasMapper.selectDatasByID(table, labelHistoryVO.getDataId());
                Map<String, Object> valueMap = (Map<String, Object>) stringMapMap.values().toArray()[0];
                String dataPath = (String) valueMap.get("data_path");
                labelHistoryVO.setDataPath(dataPath);
                String file = FileUtils.getBase64(dataPath);
                labelHistoryVO.setFile(file);
            }catch (IOException e){

            }

        });
        return labelHistoryVOList;
    }

    @Override
    public void addLabelHistory(LabelHistory labelHistory){
        labelHistoryMapper.addLabelHistory(labelHistory);
    }

    @Override
    public LabelHistory judgeDifference(DatasVO before, DatasVO after){
        Map<String, String> beforeLabelMap = before.getLabelList();
        Map<String, String> afterLabelMap = after.getLabelList();
        Map<String, Pos> differenceBefore = new HashMap<>();
        Map<String, Pos> differenceAfter = new HashMap<>();
        for (String key : beforeLabelMap.keySet()) {
            //是值、坐标  不是测试结果等
            if (( StringUtils.contains(key, "value")|| StringUtils.contains(key, "pos"))
                    && !(Objects.equals(beforeLabelMap.get(key), afterLabelMap.get(key)))){
                String[] splits = key.split("_");
                Pos posBefore = null;
                Pos posAfter = null;
                if (differenceBefore.containsKey(splits[0])){
                    posBefore = differenceBefore.get(splits[0]);
                    posAfter = differenceAfter.get(splits[0]);
                }else {
                    posBefore = new Pos();
                    posAfter = new Pos();
                    posBefore.setName(splits[0]);
                    posAfter.setName(splits[0]);
                }

                if (splits[1].equals("value")){
                    posBefore.setValue(beforeLabelMap.get(key));
                    posAfter.setValue(afterLabelMap.get(key));
                }else {
                    posBefore.setPosition(JSON.parseObject(beforeLabelMap.get(key), Position.class));
                    posAfter.setPosition(JSON.parseObject(afterLabelMap.get(key), Position.class));
                }
                differenceBefore.put(splits[0], posBefore);
                differenceAfter.put(splits[0], posAfter);
            }
        }
        LabelHistory labelHistory = new LabelHistory();
        labelHistory.setActionType(ActionType.ALL_CHANGE.getCode());
        labelHistory.setBeforeAction(JSON.toJSONString(differenceBefore));
        labelHistory.setAfterAction(JSON.toJSONString(differenceAfter));
        return labelHistory;
    }

    @Override
    public LabelHistoryVO getLabelHistoryVO(Integer datasetID, Integer labelHistoryID) {
        try {
            LabelHistoryVO labelHistoryVO = labelHistoryMapper.getLabelHistoryVO(labelHistoryID);
            String table = DatasetUtils.getDataTable(datasetID);
            Map<String, Map<String, Object>> stringMapMap = datasMapper.selectDatasByID(table, labelHistoryVO.getDataId());
            Map<String, Object> valueMap = (Map<String, Object>) stringMapMap.values().toArray()[0];
            String dataPath = (String) valueMap.get("data_path");
            labelHistoryVO.setDataPath(dataPath);
            String file = FileUtils.getBase64(dataPath);
            labelHistoryVO.setFile(file);
            return labelHistoryVO;
        } catch (IOException e) {
            throw new BusinessException(ResponseCode.FILE_ERROR, "读取文件错误");
        }
    }
}
