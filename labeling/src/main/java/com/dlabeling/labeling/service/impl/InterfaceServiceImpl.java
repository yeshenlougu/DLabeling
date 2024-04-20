package com.dlabeling.labeling.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.dlabeling.common.enums.ResponseCode;
import com.dlabeling.common.exception.BusinessException;
import com.dlabeling.common.exception.file.FileNotFileException;
import com.dlabeling.common.utils.FileUtils;
import com.dlabeling.common.utils.RequestUtils;
import com.dlabeling.labeling.common.LabelConstant;
import com.dlabeling.labeling.core.enums.InterfaceType;
import com.dlabeling.labeling.domain.json.LabelJson;
import com.dlabeling.labeling.domain.json.Pos;
import com.dlabeling.labeling.domain.po.*;
import com.dlabeling.labeling.domain.vo.*;
import com.dlabeling.labeling.domain.vo.item.LabelHistoryItem;
import com.dlabeling.labeling.enums.SplitType;
import com.dlabeling.labeling.mapper.*;
import com.dlabeling.labeling.service.InterfaceService;
import com.dlabeling.labeling.utils.DatasetUtils;
import com.dlabeling.labeling.utils.LabelWriteUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/18
 */
@Slf4j
@Service
public class InterfaceServiceImpl implements InterfaceService {

    @Autowired
    InterfaceHistoryMapper interfaceHistoryMapper;

    @Autowired
    DataSplitMapper dataSplitMapper;

    @Autowired
    DatasMapper datasMapper;

    @Autowired
    DatasetsMapper datasetsMapper;

    @Autowired
    InterfaceAddressMapper interfaceAddressMapper;

    @Autowired
    LabelConfMapper labelConfMapper;

    @Override
    public void addInterfaceAddress(InterfaceAddressVO interfaceAddressVO){
        InterfaceAddress interfaceAddress = InterfaceAddressVO.convertToInterfaceAddress(interfaceAddressVO);
        interfaceAddress.setCreateTime(new Date());
        interfaceAddressMapper.addInterfaceAddress(interfaceAddress);
    }

    @Override
    public void addInterfaceHistory(InterfaceHistory interfaceHistory) {
        interfaceHistory.setCreateTime(new Date());
        interfaceHistoryMapper.addInterfaceHistory(interfaceHistory);
    }

    @Override
    public List<InterfaceHistoryVO> getInterfaceHistoryList(Integer datasetID, String type) {
//        List<InterfaceHistoryVO> interfaceHistoryVOList = interfaceHistoryMapper.selectAllInterfaceHistory();
        List<InterfaceHistoryVO> interfaceHistoryVOList = interfaceHistoryMapper.selectInterfaceHistoryBySplitType(datasetID, SplitType.getSplitTypeByType("test").getCode());
        interfaceHistoryVOList.forEach(interfaceHistoryVO -> {
            if (interfaceHistoryVO.getInterfaceAddressVO().getDestroyTime() !=null){
                interfaceHistoryVO.setName(interfaceHistoryVO.getName()+"(接口已删除)");
                interfaceHistoryVO.getInterfaceAddressVO().setInterfaceName(interfaceHistoryVO.getInterfaceAddressVO().getInterfaceName()+"(接口已删除)");
            }
            interfaceHistoryVO.setType(InterfaceType.getInterfaceTypeByCode(Integer.parseInt(interfaceHistoryVO.getType())).getDescription());//映射interfaceHistoryType
            interfaceHistoryVO.getInterfaceAddressVO().setInterfaceType(
                    InterfaceType.getInterfaceTypeByCode(Integer.parseInt(
                            interfaceHistoryVO.getInterfaceAddressVO().getInterfaceType()
                            )).getDescription());
            interfaceHistoryVO.getSplitVO().setType(
                    SplitType.getSplitTypeByCode(Integer.parseInt(
                            interfaceHistoryVO.getSplitVO().getType()
                    )).getDescription()
            );
        });
        return interfaceHistoryVOList;
    }

    @Override
    @Transactional
    public void doLabelInterface(DoLabelVO doLabelVO){
        String doLabelDir = null;
 
        if (doLabelVO.getName()==null){
            doLabelVO.setName(doLabelVO.getLabelType()+"_temp");
        }
        if (doLabelVO.getLabelType().equals("test")){
            DataSplit dataSplit = new DataSplit();
            dataSplit.setSplitId(doLabelVO.getSplitId());
            List<DataSplit> dataSplitList = dataSplitMapper.selectDataSplit(dataSplit);
            doLabelVO.setDatasIdList(dataSplitList.stream().map(DataSplit::getDataId).collect(Collectors.toList()));
        }
        // 获取数据库信息
        String table = DatasetUtils.getDataTable(doLabelVO.getDatasetId());
        Datasets datasets = datasetsMapper.selectByID(doLabelVO.getDatasetId());
        String dataRootPath = datasets.getDataRootDir();
        InterfaceAddress interfaceAddress = interfaceAddressMapper.selectInterfaceAddressByID(doLabelVO.getInterfaceId());
        String interfaceUrl = interfaceAddress.getInterfaceAddress();
        // 获取 待接口 数据
        Map<String, Map<String, Object>> stringMapMap = datasMapper.selectDatasByIDList(table, doLabelVO.getDatasIdList());
        doLabelDir = FileUtils.resolvePath(dataRootPath, FileUtils.resolvePath(doLabelVO.getLabelType(), doLabelVO.getName()));
        if (FileUtils.exists(doLabelDir)){
            FileUtils.deleteDir(doLabelDir);
        }
        FileUtils.makeDir(doLabelDir);
        
        // 获取 labelList
        List<LabelConf> labelConfByDB = labelConfMapper.getLabelConfByDB(doLabelVO.getDatasetId());
        List<String> collectNameList = labelConfByDB.stream().map(labelConf -> labelConf.getLabelName()).collect(Collectors.toList());
        Map<String, String> fieldToLabel = new HashMap<>();
        Map<String, String> labelTofield = new HashMap<>();
        labelConfByDB.forEach(labelConf -> {
            String field = LabelConstant.DATA_FILED_PREF + "_" + labelConf.getId();
            fieldToLabel.put(field, labelConf.getLabelName());
            labelTofield.put(labelConf.getLabelName(), field);
        });
        InterfaceHistory interfaceHistory = new InterfaceHistory();
        interfaceHistory.setInterfaceId(doLabelVO.getInterfaceId());
        interfaceHistory.setName(doLabelVO.getName());
        interfaceHistory.setDatasetId(doLabelVO.getDatasetId());
        interfaceHistory.setSplitId(doLabelVO.getSplitId());
        interfaceHistory.setType(InterfaceType.getInterfaceTypeByType(doLabelVO.getLabelType()).getCode());
        addInterfaceHistory(interfaceHistory);
        for (Map<String, Object> value : stringMapMap.values()) {
            Response response=null;
            try {
                String filePath = (String) value.get("data_path");
                response = RequestUtils.sendLabelFile(interfaceUrl, new File(filePath));
                String jsonString = response.body().string();
                // 写入文件
                LabelWriteUtils.writeJSON(
                        FileUtils.resolvePath(doLabelDir,
                                FileUtils.changeSuffix(FileUtils.getFileName(filePath), "_response.json"))
                        , jsonString);
                // 更新数据库
                Datas datas = Datas.convertMapToDatas(value);
                datas.setLabelMap(new HashMap<>());
                JSONObject jsonObject = JSON.parseObject(jsonString);
                if ((Integer) jsonObject.get("code")!= 200){
                    throw new BusinessException(ResponseCode.MODEL_ERROR, (String) jsonObject.get("msg"));
                }
//               JSONObject map = JSON.parseObject(jsonString);
                LabelJson labelJson = JSON.parseObject(JSON.toJSONString(jsonObject.get("data")), LabelJson.class);
                if (!CollectionUtils.isEqualCollection(labelJson.getLabelList(), collectNameList)){
                    throw new BusinessException(ResponseCode.BUSINESS_ERROR, "标签列表错误");
                }
                LabelWriteUtils.writeJSON(
                        FileUtils.resolvePath(doLabelDir,
                                FileUtils.changeSuffix(FileUtils.getFileName(filePath), ".json"))
                        , JSON.toJSONString(labelJson));
                
                for (Pos label : labelJson.getLabels()) {
                    String filedName = labelTofield.get(label.getName()) + "_" + doLabelVO.getLabelType();
                    datas.getLabelMap().put(filedName,JSON.toJSONString(label));
                    if (doLabelVO.getLabelType().equals(InterfaceType.LABEL.getName())){
                        datas.getLabelMap().put(labelTofield.get(label.getName())+"_value", label.getValue());
                        datas.getLabelMap().put(labelTofield.get(label.getName())+"_pos", JSON.toJSONString(label.getPosition()));
                    }
                }
                datasMapper.updateDatas(datas, table);
            }catch (IOException e){
                FileUtils.deleteDir(doLabelDir);
                throw new BusinessException(ResponseCode.BUSINESS_ERROR, "写入文件错误");
            }catch (BusinessException e){
                FileUtils.deleteDir(doLabelDir);
                throw e;
            }finally {
                if (response != null){
                    response.close();
                }
                if (!doLabelVO.getLabelType().equals("test")){
                    FileUtils.deleteDir(doLabelDir);
                }
            }
        }
    }


    @Override
    public List<DatasVO> getLabelHistoryDatasList(InterfaceHistoryVO interfaceHistory) {
        List<LabelConf> labelConfByDB = labelConfMapper.getLabelConfByDB(interfaceHistory.getDatasets().getId());
        Map<String, String> fieldToLabel = new HashMap<>();
        labelConfByDB.forEach(labelConf -> {
            String field = LabelConstant.DATA_FILED_PREF + "_" + labelConf.getId();
            fieldToLabel.put(field, labelConf.getLabelName());
        });
        Datasets datasets = datasetsMapper.selectByID(interfaceHistory.getDatasets().getId());

        String basePath = DatasetUtils.labelRootPath(interfaceHistory.getName(), interfaceHistory.getType(),datasets.getDataRootDir());
        List<DatasVO> datasVOList = new ArrayList<>();


        Map<String, Map<String, Object>> datasMapMap = interfaceHistoryMapper.selectDatasOfInterfaceHistory(interfaceHistory.getId(), DatasetUtils.getDataTable(interfaceHistory.getDatasets().getId()));
        for (Map<String, Object> value : datasMapMap.values()) {
            try {
                DatasVO datasVO = DatasVO.convertMapToDatasVO(value, fieldToLabel);
                FileReader fileReader = new FileReader(FileUtils.resolvePath(basePath,FileUtils.getFileName(datasVO.getLabelPath())));
                LabelJson labelJson = JSON.parseObject(fileReader, LabelJson.class);
                for (Pos label : labelJson.getLabels()) {
                    datasVO.getLabelList().put(label.getName()+"_"+interfaceHistory.getType(), JSONObject.toJSONString(label));
                }
                datasVOList.add(datasVO);
            }catch (IOException | FileNotFileException e){
                throw new RuntimeException(e);
            }
        }
        return datasVOList;
    }

    @Override
    public DatasVO getLabelHistoryDatas(Integer interfaceHistoryID, Integer datasetID, String interfaceHistoryName, String type, Integer dataID){
        List<LabelConf> labelConfByDB = labelConfMapper.getLabelConfByDB(datasetID);
        Map<String, String> fieldToLabel = new HashMap<>();
        labelConfByDB.forEach(labelConf -> {
            String field = LabelConstant.DATA_FILED_PREF + "_" + labelConf.getId();
            fieldToLabel.put(field, labelConf.getLabelName());
        });
        Datasets datasets = datasetsMapper.selectByID(datasetID);

        String basePath = DatasetUtils.labelRootPath(interfaceHistoryName, type,datasets.getDataRootDir());
        Map<String, Map<String, Object>> datasMap = datasMapper.selectDatasByID(DatasetUtils.getDataTable(datasetID), dataID);
        DatasVO datasVO = null;
        try {
            for (Map<String, Object> value : datasMap.values()) {
                datasVO = DatasVO.convertMapToDatasVO(value, fieldToLabel);
                FileReader fileReader = new FileReader(FileUtils.resolvePath(basePath,FileUtils.getFileName(datasVO.getLabelPath())));
                LabelJson labelJson = JSON.parseObject(fileReader, LabelJson.class);
                for (Pos label : labelJson.getLabels()) {
                    datasVO.getLabelList().put(label.getName()+"_"+type, JSONObject.toJSONString(label));
                }
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        return datasVO;
    }


    @Override
    public List<InterfaceAddressVO> getInterfaceList(Integer datasetID, String type){
        InterfaceAddress interfaceAddressFilter = new InterfaceAddress();
        interfaceAddressFilter.setDatasetId(datasetID);
        InterfaceType interfaceType = InterfaceType.getInterfaceTypeByType(type);
        assert interfaceType != null;
        interfaceAddressFilter.setInterfaceType(interfaceType.getCode());
//        log.debug(interfaceAddressFilter.toString());
        List<InterfaceAddressVO> interfaceAddressList = interfaceAddressMapper.selectInterfaceByObj(interfaceAddressFilter);
        interfaceAddressList.forEach(interfaceAddressVO -> {
            interfaceAddressVO.setInterfaceType(
                    InterfaceType.getInterfaceTypeByCode(Integer.parseInt(interfaceAddressVO.getInterfaceType()))
                            .getDescription());
        });
        return interfaceAddressList;
    }

    @Override
    public List<InterfaceVO> getAllInterface() {
        List<InterfaceVO> interfaceVOList = interfaceAddressMapper.selectAllInterfaceVO();
        interfaceVOList.forEach(interfaceVO -> {
            interfaceVO.getInterfaceAddressVOList().forEach(interfaceAddressVO -> {
                interfaceAddressVO.setInterfaceType(InterfaceType.getInterfaceTypeByCode(
                        Integer.parseInt(interfaceAddressVO.getInterfaceType()))
                        .getDescription());
            });
        });
        return interfaceVOList;
    }

    @Override
    public List<LabelHistoryItem> getAllLabelHistoryVO(String type) {
        List<LabelHistoryItem> labelHistoryItemList = interfaceHistoryMapper.getAllLabelHistoryItem(InterfaceType.getInterfaceTypeByType(type).getCode());
        labelHistoryItemList.forEach(labelHistoryItem -> labelHistoryItem.getInterfaceHistoryVOList()
                .forEach(labelHistoryVO->{
                    if (labelHistoryVO.getInterfaceAddressVO().getDestroyTime() != null){
                        labelHistoryVO.getInterfaceAddressVO().setInterfaceName(labelHistoryVO.getInterfaceAddressVO().getInterfaceName()+"(接口已删除)");
                    }
                    labelHistoryVO.setType(InterfaceType.getInterfaceTypeByCode(Integer.parseInt(labelHistoryVO.getType())).getDescription());
                    labelHistoryVO.getInterfaceAddressVO().setInterfaceType(
                            InterfaceType.getInterfaceTypeByCode(Integer.parseInt((labelHistoryVO.getInterfaceAddressVO().getInterfaceType()))).getDescription());
                    labelHistoryVO.getSplitVO().setType(
                            SplitType.getSplitTypeByCode(Integer.parseInt(labelHistoryVO.getSplitVO().getType())).getDescription());
        }));
        return labelHistoryItemList;
    }


    @Override
    public void updateInterfaceAddress(InterfaceAddressVO interfaceAddressVO) {
        InterfaceAddress interfaceAddress = InterfaceAddressVO.convertToInterfaceAddress(interfaceAddressVO);
        interfaceAddress.setUpdateTime(new Date());
        interfaceAddress.setId(interfaceAddressVO.getId());
        
        interfaceAddressMapper.updateInterfaceAddress(interfaceAddress);
    }

    @Override
    public void deleteInterfaceAddress(Integer id) {
        interfaceAddressMapper.deleteInterfaceAddressByID(id, new Date());
    }
}
