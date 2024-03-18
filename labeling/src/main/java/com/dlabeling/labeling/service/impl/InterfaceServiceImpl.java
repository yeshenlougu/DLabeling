package com.dlabeling.labeling.service.impl;

import com.dlabeling.common.exception.file.FileNotFileException;
import com.dlabeling.common.utils.FileUtils;
import com.dlabeling.common.utils.RequestUtils;
import com.dlabeling.labeling.common.LabelConstant;
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
        String table = DatasetUtils.getDataTable(doLabelVO.getDatasetId());
        Datasets datasets = datasetsMapper.selectByID(doLabelVO.getDatasetId());
        String dataRootPath = datasets.getDataRootDir();
        InterfaceAddress interfaceAddress = interfaceAddressMapper.selectInterfaceAddressByID(doLabelVO.getInterfaceId());
        String interfaceUrl = interfaceAddress.getInterfaceAddress();

        Map<String, Map<String, Object>> stringMapMap = datasMapper.selectDatasByIDList(table, doLabelVO.getDatasIdList());
        String doLabelDir = FileUtils.resolvePath(dataRootPath, FileUtils.resolvePath(doLabelVO.getLabelType(), doLabelVO.getName()));
        FileUtils.makeDir(doLabelDir);


        InterfaceHistory interfaceHistory = new InterfaceHistory();
        interfaceHistory.setInterfaceId(doLabelVO.getInterfaceId());
        interfaceHistory.setName(doLabelVO.getName());
        interfaceHistory.setDatasetId(doLabelVO.getDatasetId());
        interfaceHistory.setSplitId(doLabelVO.getSplitId());

        addInterfaceHistory(interfaceHistory);


        for (Map<String, Object> value : stringMapMap.values()) {
            try {
                String filePath = (String) value.get("data_path");
                Response response = RequestUtils.labelFile(interfaceUrl, new File(filePath));
                String jsonString = response.body().string();

                LabelWriteUtils.writeJSON(
                        FileUtils.resolvePath(doLabelDir,
                                FileUtils.changeSuffix(FileUtils.getFileName(filePath), "json"))
                        , jsonString);

                if (!doLabelVO.getLabelType().equals("test")){
                    updateDatas(jsonString, table, (Integer)value.get("id"));
                }

            }catch (IOException e){

            }
        }

    }

    private void updateDatas(String jsonSting, String table, Integer id){

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
