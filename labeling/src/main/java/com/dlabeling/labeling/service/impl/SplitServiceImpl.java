package com.dlabeling.labeling.service.impl;

import com.dlabeling.common.enums.ResponseCode;
import com.dlabeling.common.exception.BusinessException;
import com.dlabeling.labeling.common.LabelConstant;
import com.dlabeling.labeling.domain.po.DataSplit;
import com.dlabeling.labeling.domain.po.LabelConf;
import com.dlabeling.labeling.domain.po.Split;
import com.dlabeling.labeling.domain.vo.DatasVO;
import com.dlabeling.labeling.domain.vo.SetItem;
import com.dlabeling.labeling.domain.vo.SplitVO;
import com.dlabeling.labeling.enums.SplitType;
import com.dlabeling.labeling.mapper.DataSplitMapper;
import com.dlabeling.labeling.mapper.DatasMapper;
import com.dlabeling.labeling.mapper.LabelConfMapper;
import com.dlabeling.labeling.mapper.SplitMapper;
import com.dlabeling.labeling.service.SplitService;
import com.dlabeling.labeling.utils.DatasetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/25
 */
@Service
public class SplitServiceImpl implements SplitService {

    @Autowired
    SplitMapper splitMapper;

    @Autowired
    DataSplitMapper dataSplitMapper;

    @Autowired
    DatasMapper datasMapper;

    @Autowired
    LabelConfMapper labelConfMapper;

    @Override
    public List<SplitVO> getSplitVOListByID(Integer datasetId, String type) {
        int type2 =  SplitType.getSplitTypeByType(type).getCode();
        List<Split> splits = splitMapper.selectByDatasetId(datasetId, type2);
        List<SplitVO> splitVOList = splits.stream().map(SplitVO::convertToSplitVO).collect(Collectors.toList());
        return splitVOList;
    }

    @Override
    public List<DatasVO> getSplitDatas(Integer datasetID, Integer splitID) {
        // 查询数据
        String table = DatasetUtils.getDataTable(datasetID);
        Map<String, Map<String, Object>> selectedDatasMap = datasMapper.selectDatasBySplit(table, datasetID, splitID);

        Map<String, String> fieldToLabel = new HashMap<>();

        List<LabelConf> labelConfByDB = labelConfMapper.getLabelConfByDB(datasetID);
        labelConfByDB.forEach(labelConf -> {
            String field = LabelConstant.DATA_FILED_PREF + "_" + labelConf.getId();
            fieldToLabel.put(field, labelConf.getLabelName());
        });
        List<DatasVO> datasVOList = new ArrayList<>();
        for (Map<String, Object> value: selectedDatasMap.values()){
            try {
                DatasVO datasVO = DatasVO.convertMapToDatasVO(value, fieldToLabel);

                datasVOList.add(datasVO);
            } catch (IOException e) {
                continue;
            }
        }
        return datasVOList;
    }

    @Override
    public void addDataToSplit(Integer datasetID, Integer splitID, ArrayList<Integer> dataIdList) {
        try {
            dataSplitMapper.batchAddDataSplit(datasetID, splitID, dataIdList);
        }catch (Exception e){
            if (e.getMessage().contains("Duplicate")){
                throw new BusinessException(ResponseCode.SQL_INSERT_ERROR, "数据已存在");
            }
            throw new BusinessException(ResponseCode.BUSINESS_ERROR, e.getMessage());
        }
    }

    @Override
    public SplitVO addSplit(SplitVO splitVO) {
        Split split = SplitVO.convertToSplit(splitVO);
        split.setCreateTime(new Date());
        splitMapper.addSplit(split);
        Split split1 = splitMapper.selectSplit(split);
        return SplitVO.convertToSplitVO(split1);
    }

    @Override
    public void deleteSplit(Integer splitID) {
        splitMapper.deleteSplitByID(splitID);
        dataSplitMapper.batchDeleteBySplitID(splitID);
    }

    @Override
    public void updateSplit(SplitVO splitVO) {
        splitMapper.updateSplit(splitVO);
    }

    @Override
    public List<SetItem> getAllSplitByType(String type) {
        List<SetItem> setItemList = new ArrayList<>();
        SplitType splitTypeByType = SplitType.getSplitTypeByType(type);
        setItemList = splitMapper.getSetItemList(splitTypeByType.getCode());

        setItemList.forEach(setItem -> {
            setItem.getSplitVOList().forEach(splitVO -> splitVO.setType(
                    SplitType.getSplitTypeByCode(Integer.parseInt(splitVO.getType()))
                            .getDescription()));
        });
        return setItemList;
    }

    @Override
    public void deleteData(Integer datasetID, Integer splitID, Integer dataID) {
        DataSplit dataSplit = new DataSplit();
        dataSplit.setDatasetId(datasetID);
        dataSplit.setSplitId(splitID);
        dataSplit.setDataId(dataID);
        dataSplitMapper.deleteDataSplit(dataSplit);
    }
}
