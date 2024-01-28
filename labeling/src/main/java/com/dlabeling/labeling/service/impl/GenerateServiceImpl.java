package com.dlabeling.labeling.service.impl;

import com.dlabeling.common.exception.file.FileException;
import com.dlabeling.common.utils.FileUtils;
import com.dlabeling.common.utils.StringUtils;
import com.dlabeling.labeling.common.DBCreateConstant;
import com.dlabeling.labeling.common.LabelConstant;
import com.dlabeling.labeling.core.enums.DataBaseType;
import com.dlabeling.labeling.domain.po.Datasets;
import com.dlabeling.labeling.domain.po.LabelConf;
import com.dlabeling.labeling.domain.vo.DatasetsVO;
import com.dlabeling.labeling.domain.vo.LabelConfVO;
import com.dlabeling.labeling.generate.DBManager;
import com.dlabeling.labeling.mapper.DatasetsMapper;
import com.dlabeling.labeling.mapper.LabelConfMapper;
import com.dlabeling.labeling.service.GenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
@Service
public class GenerateServiceImpl implements GenerateService {

    @Autowired
    DatasetsMapper datasetsMapper;

    @Autowired
    LabelConfMapper labelConfMapper;

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DBManager dbManager;

    @Override
    @Transactional
    public void makeDataBase(DatasetsVO datasetsVO) throws FileException{
        Datasets datasets = DatasetsVO.convertToDatasets(datasetsVO);
        datasetsMapper.addDatasets(datasets);
        Datasets selectDatasets = datasetsMapper.selectByObj(datasets);
        String dbRootDir = datasetsVO.getDataRootDir();

        // 创建数据集存储目录
        createDataBaseDir(dbRootDir);

        // 生成Label文件
        createLabelTxt(dbRootDir, datasetsVO.getLabelConfVOList());

        List<LabelConf> labelConfList = datasetsVO.getLabelConfVOList().stream()
                .map(LabelConfVO::convertToLabelConf)
                .collect(Collectors.toList());

        labelConfMapper.batchAddLabelConf(labelConfList);


        List<LabelConf> labelConfByDB = labelConfMapper.getLabelConfByDB(selectDatasets.getId());

        createDBDataTable(selectDatasets, labelConfByDB);




    }

    /**
     * 创建数据集目录
     *
     * @param rootPath
     */
    private void createDataBaseDir(String rootPath){
        FileUtils.makeDir(rootPath);
        FileUtils.makeDir(FileUtils.resolvePath(rootPath, LabelConstant.DATA_DIR_NAME));
        FileUtils.makeDir(FileUtils.resolvePath(rootPath, LabelConstant.LABEL_DIR_NAME));
        FileUtils.makeDir(FileUtils.resolvePath(rootPath, LabelConstant.AUTO_DIR_NAME));
        FileUtils.makeDir(FileUtils.resolvePath(rootPath, LabelConstant.TEST_DIR_NAME));
        FileUtils.makeDir(FileUtils.resolvePath(rootPath, LabelConstant.CHECK_DIR_NAME));
    }

    private void createLabelTxt(String rootPath, List<LabelConfVO> labelConfVOS){
        String filePath = FileUtils.resolvePath(rootPath, LabelConstant.LABEL_TXT_FILE);
        FileUtils.makeFile(filePath);

        String content = labelConfVOS.stream()
                .map(LabelConfVO::getLabelName)
                .collect(Collectors.joining("\n"));
        FileUtils.writeString(filePath, content);
    }


    private void createDBDataTable(Datasets datasets) throws SQLException {
        DataBaseType dbType = DataBaseType.getByCode(datasets.getType());
        Integer dataBaseId = datasets.getId();

        String dbName = createTableName(dataBaseId);

        String createTableSQL = StringUtils.format(DBCreateConstant.CREATE_DATABASE, dbName);

////        jdbcTemplate.execute(createTableSQL);
//        Connection connection = dataSource.getConnection();
//        connection.setCatalog();
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL);


//        jdbcTemplate.execute();

    }

//    private String createDBName(Integer dataBaseId){ return LabelConstant.DB_GEN_PREF + "_" + dataBaseId;}

    private String createTableName(Integer dataBaseID){
        return LabelConstant.TABLE_GEN_PREF + "_" + dataBaseID;
    }

    private void createDBDataTable(Datasets datasets, List<LabelConf> labelConfList){
        String newTableName = createTableName(datasets.getId());
        StringBuilder createDBTableSQL = new StringBuilder();

        createDBTableSQL.append(StringUtils.format(DBCreateConstant.DROP_TABLE, newTableName));
        // 添加建表
        createDBTableSQL.append(StringUtils.format(DBCreateConstant.CREATE_TABLE, newTableName)).append("(");

        createDBTableSQL.append(DBCreateConstant.ID_FIELD).append(DBCreateConstant.DATA_PATH_FIELD).append(DBCreateConstant.LABEL_PATH_FIELD);
//        for (LabelConf labelConf : labelConfList) {
//            String labelBaseName = LabelConstant.DATA_FILED_PREF + "_" + labelConf.getId();
//            createDBTableSQL.append(StringUtils.format(DBCreateConstant.LABEL_FIELD,
//                    labelBaseName + "_" + LabelConstant.DATA_FILED_VALUE,
//                    labelBaseName + "_" + LabelConstant.DATA_FILED_POSITION,
//                    labelBaseName + "_" + LabelConstant.DATA_FILED_AUTO,
//                    labelBaseName + "_" + LabelConstant.DATA_FILED_TEST));
//        }

        createDBTableSQL.append(
                labelConfList.stream()
                        .map(labelConf -> {
                            String labelBaseName = LabelConstant.DATA_FILED_PREF + "_" + labelConf.getId();
                            return StringUtils.format(DBCreateConstant.LABEL_FIELD,
                                    labelBaseName + "_" + LabelConstant.DATA_FILED_VALUE,
                                    labelBaseName + "_" + LabelConstant.DATA_FILED_POSITION,
                                    labelBaseName + "_" + LabelConstant.DATA_FILED_AUTO,
                                    labelBaseName + "_" + LabelConstant.DATA_FILED_TEST);
                        })
                        .collect(Collectors.joining()));


        createDBTableSQL.append(");");
        String createSQL = createDBTableSQL.toString();

        dbManager.execute(createSQL);

        return ;
    }


    private Boolean isAbsoluteDir(String path){
        return Boolean.TRUE;
    }

    @Override
    public void makeLabelFile(File file) {

    }


}
