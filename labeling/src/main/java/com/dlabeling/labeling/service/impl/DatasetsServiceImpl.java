package com.dlabeling.labeling.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.dlabeling.common.enums.ResponseCode;
import com.dlabeling.common.exception.BusinessException;
import com.dlabeling.common.exception.file.FileNotFileException;
import com.dlabeling.common.utils.FileUtils;
import com.dlabeling.common.utils.ServletUtils;
import com.dlabeling.common.utils.StringUtils;
import com.dlabeling.framework.web.TokenService;
import com.dlabeling.labeling.common.DBCreateConstant;
import com.dlabeling.labeling.common.LabelConstant;
import com.dlabeling.labeling.domain.po.*;
import com.dlabeling.labeling.domain.vo.*;
import com.dlabeling.labeling.domain.vo.SetItem;
import com.dlabeling.labeling.enums.SplitType;
import com.dlabeling.labeling.generate.DBManager;
import com.dlabeling.labeling.mapper.*;
import com.dlabeling.labeling.service.DatasetsService;
import com.dlabeling.labeling.utils.DatasetUtils;
import com.dlabeling.labeling.utils.LabelWriteUtils;
import com.dlabeling.system.domain.po.user.UserInfo;
import com.dlabeling.system.domain.vo.LoginUser;
import com.dlabeling.system.mapper.user.UserInfoMapper;
import com.dlabeling.system.service.user.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private InterfaceAddressMapper interfaceAddressMapper;

    @Autowired
    DBManager dbManager;

    @Autowired
    TokenService tokenService;

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
    public List<DatasVO> getDatasBySetID(Integer id){
        List<LabelConf> labelConfByDB = labelConfMapper.getLabelConfByDB(id);
        Map<String, String> fieldToLabel = new HashMap<>();
        labelConfByDB.forEach(labelConf -> {
            String field = LabelConstant.DATA_FILED_PREF + "_" + labelConf.getId();
            fieldToLabel.put(field, labelConf.getLabelName());
        });

        String tableName = DatasetUtils.getDataTable(id);
//        Integer limit = end - start;

        Map<String, Map<String, Object>> stringObjectMap = datasMapper.selectDataLimit(tableName);

        List<DatasVO> datasVOList = new ArrayList<>();

        for (Map<String, Object> value : stringObjectMap.values()) {
            try {
                DatasVO datasVO = DatasVO.convertMapToDatasVO(value, fieldToLabel);
                datasVOList.add(datasVO);
            }catch (IOException | FileNotFileException e){
                continue;
            }

        }

        return datasVOList;
    }

    @Override
    public List<DatasVO> getDatasByFilter(DatasFilterVO datasFilterVO) {
        String tableName = DatasetUtils.getDataTable(datasFilterVO.getId());

        // label_id => label_name的映射
        Map<String, String> fieldToLabel = new HashMap<>();
        List<LabelConf> labelConfByDB = labelConfMapper.getLabelConfByDB(datasFilterVO.getId());
        labelConfByDB.forEach(labelConf -> {
            String field = LabelConstant.DATA_FILED_PREF + "_" + labelConf.getId();
            fieldToLabel.put(field, labelConf.getLabelName());
        });

        // 获取过滤后的数据
        Map<String, Map<String, Object>> selectedMap =  datasMapper.selectDataFilterLimit(datasFilterVO.getLabelValueMap(),tableName);
        List<DatasVO> datasVOList = new ArrayList<>();
        for (Map<String, Object> value : selectedMap.values()) {
            try{
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
            }catch (IOException e){
                continue;
            }
        }
        return datasVOList;
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
    public DatasVO getDatasByID(Integer datasetID, Integer dataID) {
        String table = DatasetUtils.getDataTable(datasetID);
        Map<String, Map<String, Object>> selectedDatas = datasMapper.selectDatasByID(table, dataID);
        DatasVO datasVO = new DatasVO();
        List<LabelConf> labelConfByDB = labelConfMapper.getLabelConfByDB(datasetID);
        Map<String, String> fieldToLabel = new HashMap<>();
        labelConfByDB.forEach(labelConf -> {
            String field = LabelConstant.DATA_FILED_PREF + "_" + labelConf.getId();
            fieldToLabel.put(field, labelConf.getLabelName());
        });
        for (Map<String, Object> value : selectedDatas.values()) {
            try {
                datasVO = DatasVO.convertMapToDatasVO(value, fieldToLabel);
            }catch (IOException e){

            }
        }
        return datasVO;
    }

    @Override
    public void batchEditDatas(DatasEditVO datasEditVO) {
//        datasMapper.

        int datasetID = datasEditVO.getId();
        Map<String, Integer> editForm = datasEditVO.getEditForm();
        List<Integer> editDatasList = datasEditVO.getDatasList();
        List<String> editLabelList = datasEditVO.getLabelList();
        if (editLabelList==null || editLabelList.isEmpty()){
            throw new BusinessException(ResponseCode.PARAMETER_ERROR, "待修改标签不能为空");
        }
        if (editDatasList == null || editDatasList.isEmpty()){
            throw new BusinessException(ResponseCode.PARAMETER_ERROR, "待修改数据不能为空");
        }
        if (editForm.get("top")==0 && editForm.get("right")==0 && editForm.get("bottom")==0 && editForm.get("left")==0){
            throw new BusinessException(ResponseCode.PARAMETER_ERROR, "坐标为改变");
        }

        List<LabelConf> labelConfByDB = labelConfMapper.getLabelConfByDB(datasetID);
        List<Integer> labelIDList = labelConfByDB.stream().filter(labelConf -> editLabelList.contains(labelConf.getLabelName())).map(LabelConf::getId).collect(Collectors.toList());
        List<String> labelNameList = labelConfByDB.stream().map(LabelConf::getLabelName).collect(Collectors.toList());
        Map<String, String> fieldToLabel = new HashMap<>();
        labelConfByDB.forEach(labelConf -> {
            String field = LabelConstant.DATA_FILED_PREF + "_" + labelConf.getId();
            fieldToLabel.put(field, labelConf.getLabelName());
        });

        String table = DatasetUtils.getDataTable(datasetID);


        // 改变pos
        Map<String, Map<String, Object>> selectedDatas = datasMapper.selectDatasByIDList(table, editDatasList);

        List<Datas> datasList = new ArrayList<>();

        for (Map<String, Object> value : selectedDatas.values()) {
            Datas datas = new Datas();
            datas.setId((Integer) value.get("id"));
            datas.setDataPath((String) value.get("data_path"));
            datas.setLabelPath((String) value.get("label_path"));
            Map<String, Object> labelMap = new HashMap<>();

            for (String key : value.keySet()) {
                if (StringUtils.contains(key, "pos")){
                    Map<String, Integer> pos = JSON.parseObject((String) value.get(key), new TypeReference<Map<String, Integer>>() {});
                    if (pos != null){
                        pos.put("x",pos.get("x") - editForm.get("top"));
                        pos.put("y", pos.get("y") - editForm.get("left"));
                        pos.put("w", pos.get("w") + editForm.get("right") + editForm.get("left"));
                        pos.put("h", pos.get("h") + editForm.get("bottom") + editForm.get("top"));
                        labelMap.put(key, JSON.toJSONString(pos));
                    }
                }
            }
            datas.setLabelMap(labelMap);
            datasList.add(datas);
        }
        // 批量修改 datas
        for (Datas datas : datasList) {
            try {
                datasMapper.updateDatas(datas, table);
                DatasVO datasVO = DatasVO.convertDatasToDatasVO(datas, fieldToLabel);
                LabelWriteUtils.writeLabelJSON(datasVO.getLabelPath(), datasVO, labelNameList);
            }catch (IOException e){
                continue;
            }

        }
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
    public List<DatasetsVO> getDatasetByFilter(DatasetsVO datasetsVO) {
//        Datasets datasetsFilter = DatasetsVO.convertToDatasets(datasetsVO);
        List<DatasetsVO> datasetsVOList = datasetsMapper.selectDatasetByJoinBatchWithFilter(datasetsVO);
        return datasetsVOList;
    }


    @Override
    public List<String> getCreatorList(){
        List<String> creatorList = datasetsMapper.fetchCreatorList();
        return creatorList;
    }

    @Override
    public void addDataToSplit(Integer datasetID, Integer splitID, List<Integer> dataIdList) {
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
    public void updateDatas(DatasVO datasVO) {
        try {
            Set<String> labelName = new HashSet<>();
            Map<String, Object> labelObject = new HashMap<>();
            List<LabelConf> labelConfByDB = labelConfMapper.getLabelConfByDB(datasVO.getDatasetID());
            Map<String, Integer> labelName2ID = new HashMap<>();
            labelConfByDB.forEach(labelConf -> labelName2ID.put(labelConf.getLabelName(), labelConf.getId()));
            Map<String, String> fieldToLabel = new HashMap<>();
            labelConfByDB.forEach(labelConf -> {
                String field = LabelConstant.DATA_FILED_PREF + "_" + labelConf.getId();
                fieldToLabel.put(field, labelConf.getLabelName());
            });

            for (Map.Entry<String, String> entry : datasVO.getLabelList().entrySet()) {
                String key = entry.getKey();
                String[] split = key.split("_");
                labelObject.put("label_"+labelName2ID.get(split[0])+"_" + split[1], entry.getValue());
            }

            String table = DatasetUtils.getDataTable(datasVO.getDatasetID());
            Datas datas = new Datas();
            datas.setId(datasVO.getId());
            datas.setLabelMap(labelObject);
            datasMapper.updateDatas(datas, table);
            Map<String, Map<String, Object>> stringMapMap = datasMapper.selectDatasByID(table, datas.getId());
            for (Map<String, Object> value : stringMapMap.values()) {
                datas.setDataPath((String) value.get("data_path"));
                datas.setLabelPath((String) value.get("label_path"));
            }

            DatasVO datasVO1 = DatasVO.convertDatasToDatasVO(datas, fieldToLabel);
            LabelWriteUtils.writeLabelJSON(datasVO1.getLabelPath(), datasVO1, new ArrayList<>(labelName2ID.keySet()));
        }catch (IOException e){

        }


    }

    @Override
    @Transactional
    public void updateDatasetInfo(DatasetsVO datasetsVO) {
        Datasets datasets = DatasetsVO.convertToDatasets(datasetsVO);
        datasetsMapper.updateDatasetsByID(datasets);

        List<LabelConf> labelConfByDB = labelConfMapper.getLabelConfByDB(datasetsVO.getId());
        List<LabelConfVO> labelConfVOList = datasetsVO.getLabelConfList();
        List<LabelConf> labelConfList = labelConfVOList.stream().map(LabelConfVO::convertToLabelConf).collect(Collectors.toList());
        if (labelConfList.size() != labelConfByDB.size()){
            // 交集
            List<LabelConf> intersection = new ArrayList<>(labelConfByDB);
            intersection.retainAll(labelConfList);

            // 要delete的label
            labelConfByDB.removeAll(intersection);

            String table = DatasetUtils.getDataTable(datasets.getId());

            for (LabelConf labelConf : labelConfByDB) {
                String baseFiledName = "label_" + labelConf.getId();
                List<String> sqlFiled = new ArrayList<>();
                sqlFiled.add(StringUtils.format(DBCreateConstant.DELETE_COLUMN, table, baseFiledName+"_"+LabelConstant.DATA_FILED_VALUE));
                sqlFiled.add(StringUtils.format(DBCreateConstant.DELETE_COLUMN, table, baseFiledName+"_"+LabelConstant.DATA_FILED_POSITION));
                sqlFiled.add(StringUtils.format(DBCreateConstant.DELETE_COLUMN, table, baseFiledName+"_"+LabelConstant.DATA_FILED_AUTO));
                sqlFiled.add(StringUtils.format(DBCreateConstant.DELETE_COLUMN, table, baseFiledName+"_"+LabelConstant.DATA_FILED_TEST));
                for (String sql : sqlFiled) {
                    dbManager.execute(sql);
                }

                labelConfMapper.deleteLabelConfByID(labelConf.getId());
            }
            // 要添加的
            labelConfList.removeAll(intersection);
            for (LabelConf labelConf : labelConfList) {
                labelConfMapper.addLabelConf(labelConf);
                LabelConf selectByObj = labelConfMapper.selectByObj(labelConf);

                String baseFiledName = "label_" + selectByObj.getId();
                List<String> sqlFiled = new ArrayList<>();
                sqlFiled.add(StringUtils.format(DBCreateConstant.ADD_COLUMN, table, baseFiledName+"_"+LabelConstant.DATA_FILED_VALUE));
                sqlFiled.add(StringUtils.format(DBCreateConstant.ADD_COLUMN, table, baseFiledName+"_"+LabelConstant.DATA_FILED_POSITION));
                sqlFiled.add(StringUtils.format(DBCreateConstant.ADD_COLUMN, table, baseFiledName+"_"+LabelConstant.DATA_FILED_AUTO));
                sqlFiled.add(StringUtils.format(DBCreateConstant.ADD_COLUMN, table, baseFiledName+"_"+LabelConstant.DATA_FILED_TEST));
                for (String sql : sqlFiled) {
                    dbManager.execute(sql);
                }

            }
        }

    }

    @Override
    public List<DatasetsVO> getDatasetHas() {
        try {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getHttpServletRequest());
            List<Datasets> datasetsList = datasetsMapper.getDatasetHas(loginUser.getId());
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
        }
        catch (Exception e){
            throw new BusinessException(ResponseCode.BUSINESS_ERROR, "获取拥有数据集");
        }
//        finally {
//
//        }
    }

    @Override
    public List<DatasetsVO> getDatasetDontHas() {
        try {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getHttpServletRequest());
            List<Datasets> hasDatasetsList = datasetsMapper.getDatasetHas(loginUser.getId());
            List<Datasets> allDatasetList = datasetsMapper.getAllDataset();

            List<Datasets> otherDatasetList = new ArrayList<>(allDatasetList);
            otherDatasetList.removeAll(hasDatasetsList);

            List<DatasetsVO> datasetsVOList = otherDatasetList.stream()
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
        }
        catch (Exception e){
            throw new BusinessException(ResponseCode.BUSINESS_ERROR, "获取其他数据集");
        }
    }

    @Override
    public List<SetItem> getAllSetByType(String type) {
        List<SetItem> setItemList = new ArrayList<>();
        SplitType splitTypeByType = SplitType.getSplitTypeByType(type);
        setItemList = datasetsMapper.getSetItemList(splitTypeByType.getCode());

        setItemList.forEach(setItem -> {
            setItem.getSplitVOList().forEach(splitVO -> splitVO.setType(
                    SplitType.getSplitTypeByCode(Integer.parseInt(splitVO.getType()))
                    .getDescription()));
        });
        return setItemList;
    }
}
