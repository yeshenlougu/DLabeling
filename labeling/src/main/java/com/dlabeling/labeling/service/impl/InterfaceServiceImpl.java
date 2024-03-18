package com.dlabeling.labeling.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.dlabeling.common.exception.file.FileExistsException;
import com.dlabeling.common.exception.file.FileNotFileException;
import com.dlabeling.common.utils.FileUtils;
import com.dlabeling.common.utils.RequestUtils;
import com.dlabeling.common.utils.StringUtils;
import com.dlabeling.labeling.common.LabelConstant;
import com.dlabeling.labeling.core.enums.InterfaceType;
import com.dlabeling.labeling.domain.po.*;
import com.dlabeling.labeling.domain.vo.DatasVO;
import com.dlabeling.labeling.domain.vo.DoLabelVO;
import com.dlabeling.labeling.domain.vo.InterfaceHistoryVO;
import com.dlabeling.labeling.enums.SplitType;
import com.dlabeling.labeling.mapper.*;
import com.dlabeling.labeling.service.InterfaceService;
import com.dlabeling.labeling.utils.DatasetUtils;
import com.dlabeling.labeling.utils.LabelWriteUtils;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/18
 */

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
    public void addInterfaceHistory(InterfaceHistory interfaceHistory) {
        interfaceHistoryMapper.addInterfaceHistory(interfaceHistory);
    }

    @Override
    public List<InterfaceHistoryVO> getInterfaceHistoryList(Integer datasetID, String type) {
//        List<InterfaceHistoryVO> interfaceHistoryVOList = interfaceHistoryMapper.selectAllInterfaceHistory();
        List<InterfaceHistoryVO> interfaceHistoryVOList = interfaceHistoryMapper.selectInterfaceHistoryBySplitType(datasetID, SplitType.getSplitTypeByType("test").getCode());
        return interfaceHistoryVOList;
    }

    @Override
    @Transactional
    public void doLabelInterface(DoLabelVO doLabelVO){
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
        String doLabelDir = FileUtils.resolvePath(dataRootPath, FileUtils.resolvePath(doLabelVO.getLabelType(), doLabelVO.getName()));
        FileUtils.makeDir(doLabelDir);

        // 获取 labelList
        List<LabelConf> labelConfByDB = labelConfMapper.getLabelConfByDB(doLabelVO.getDatasetId());
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
//                String jsonString = "{\"labelList\":\"[\\\"label1\\\",\\\"label2\\\",\\\"label3\\\"]\",\"id\":\"null\",\"dataPath\":\"C:\\\\Users\\\\YYS\\\\Desktop\\\\temp\\\\test1\\\\data\\\\username-login3.png\",\"labels\":[{\"name\":\"label1\",\"position\":\"\",\"value\":\"\"},{\"name\":\"label2\",\"position\":\"\",\"value\":\"\"},{\"name\":\"label3\",\"position\":\"\",\"value\":\"\"}]}";
                // 写入文件
                LabelWriteUtils.writeJSON(
                        FileUtils.resolvePath(doLabelDir,
                                FileUtils.changeSuffix(FileUtils.getFileName(filePath), ".json"))
                        , jsonString);

                // 更新数据库
                Datas datas = Datas.convertMapToDatas(value);

                JSONObject map = JSON.parseObject(jsonString);
                JSONArray labelsArray = (JSONArray) map.get(LabelConstant.INTERFACE_LABEL_DATAS);
                for (int i=0; i<labelsArray.size(); i++){
                    JSONObject label = labelsArray.getJSONObject(i);
                    String labelName = (String) label.get("name");
                    String filedName = labelTofield.get(labelName) + "_" + doLabelVO.getLabelType();
                    label.remove("name");
                    datas.getLabelMap().put(filedName, label.toString());
                }
                datasMapper.updateDatas(datas, table);

            }catch (IOException e){

            }finally {
                if (response != null){
                    response.close();
                }
            }
        }

    }


    @Override
    public List<DatasVO> getLabelHistoryDatasList(InterfaceHistoryVO interfaceHistory) {
        Map<String, Map<String, Object>> datasMapMap = interfaceHistoryMapper.selectDatasOfInterfaceHistory(interfaceHistory.getId(), DatasetUtils.getDataTable(interfaceHistory.getDatasetId()));

        List<LabelConf> labelConfByDB = labelConfMapper.getLabelConfByDB(interfaceHistory.getDatasetId());
        Map<String, String> fieldToLabel = new HashMap<>();
        labelConfByDB.forEach(labelConf -> {
            String field = LabelConstant.DATA_FILED_PREF + "_" + labelConf.getId();
            fieldToLabel.put(field, labelConf.getLabelName());
        });

        List<DatasVO> datasVOList = new ArrayList<>();
        for (Map<String, Object> value : datasMapMap.values()) {
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
}
