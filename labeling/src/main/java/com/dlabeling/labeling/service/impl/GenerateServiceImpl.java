package com.dlabeling.labeling.service.impl;

import com.dlabeling.common.enums.ResponseCode;
import com.dlabeling.common.exception.BusinessException;
import com.dlabeling.common.exception.file.DirExistsException;
import com.dlabeling.common.exception.file.FileException;
import com.dlabeling.common.exception.file.FileExistsException;
import com.dlabeling.common.exception.file.FileNotDirException;
import com.dlabeling.common.utils.FileUtils;
import com.dlabeling.common.utils.ServletUtils;
import com.dlabeling.common.utils.StringUtils;
import com.dlabeling.labeling.common.DBCreateConstant;
import com.dlabeling.labeling.common.LabelConstant;
import com.dlabeling.labeling.config.LabelConfig;
import com.dlabeling.labeling.core.enums.DataBaseType;
import com.dlabeling.labeling.domain.po.Datasets;
import com.dlabeling.labeling.domain.po.LabelConf;
import com.dlabeling.labeling.domain.vo.DatasetsVO;
import com.dlabeling.labeling.domain.vo.LabelConfVO;
import com.dlabeling.labeling.generate.DBManager;
import com.dlabeling.labeling.mapper.DatasetsMapper;
import com.dlabeling.labeling.mapper.LabelConfMapper;
import com.dlabeling.labeling.service.GenerateService;
import com.dlabeling.labeling.utils.DatasetUtils;
import com.dlabeling.system.domain.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dlabeling.framework.web.TokenService;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
@Slf4j
@Service
public class GenerateServiceImpl implements GenerateService {

    @Autowired
    DatasetsMapper datasetsMapper;

    @Autowired
    LabelConfig labelConfig;

    @Autowired
    LabelConfMapper labelConfMapper;

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DBManager dbManager;

    @Autowired
    TokenService tokenService;

    @Override
    @Transactional
    public void makeDataBase(DatasetsVO datasetsVO){
        HttpServletRequest httpServletRequest = ServletUtils.getHttpServletRequest();
        LoginUser loginUser = tokenService.getLoginUser(httpServletRequest);


        Datasets datasets = DatasetsVO.convertToDatasets(datasetsVO);
        datasets.setCreator(loginUser.getId());

        log.info(datasets.toString());
        if (datasetsVO.getDataRootDir() == null || datasetsVO.getDataRootDir().isEmpty()){
            datasets.setDataRootDir(labelConfig.getDefaultDataRootDir());
        }
        datasets.setVisible(datasetsVO.getVisible() != null ? datasetsVO.getVisible() : true);
        datasets.setType(datasetsVO.getType()!=null? datasetsVO.getType() : DataBaseType.ImageDataBase.getCode());
        datasets.setCreateTime(new Date());
        try {
            datasetsMapper.addDatasets(datasets);
        }catch (Exception e){
            if (StringUtils.contains(e.getMessage(), "datasets.name")){
                throw new BusinessException(ResponseCode.SQL_INSERT_ERROR, "数据集名称冲突");
            }
        }

        Datasets selectDatasets = datasetsMapper.selectByObj(datasets);
        String dbRootDir = datasetsVO.getDataRootDir();
        dbRootDir = FileUtils.resolvePath(dbRootDir, selectDatasets.getName());

        Boolean deleteDirFlag = false;
        try {
            // 创建数据集存储目录
            createDataBaseDir(dbRootDir);
            // 生成Label文件
            createLabelTxt(dbRootDir, datasetsVO.getLabelConfList());

            List<LabelConf> labelConfList = datasetsVO.getLabelConfList().stream()
                    .map(LabelConfVO::convertToLabelConf)
                    .peek(labelConf -> labelConf.setDatasetId(selectDatasets.getId()))
                    .collect(Collectors.toList());

            labelConfMapper.batchAddLabelConf(labelConfList);
            List<LabelConf> labelConfByDB = labelConfMapper.getLabelConfByDB(selectDatasets.getId());
            createDBDataTable(selectDatasets, labelConfByDB);

        }catch (DirExistsException e){
            throw new BusinessException(ResponseCode.DIR_EXISTS, ResponseCode.DIR_EXISTS.getMessage());
        }catch (FileNotDirException e){
            throw new BusinessException(ResponseCode.FILE_NOT_DIR, ResponseCode.FILE_NOT_DIR.getMessage());
        }catch (FileException e){
            deleteDirFlag = true;
            throw new BusinessException(ResponseCode.FILE_ERROR, e.getMessage());
        }catch (Exception e){
            deleteDirFlag = true;
            throw new BusinessException(ResponseCode.SQL_INSERT_ERROR, e.getMessage());
        }
        finally {
            if (deleteDirFlag){
                FileUtils.deleteDir(dbRootDir);
            }
        }


    }

    /**
     * 创建数据集目录
     *
     * @param rootPath
     */
    private void createDataBaseDir(String rootPath){
        try {
            createDir(rootPath);
            createDir(FileUtils.resolvePath(rootPath, LabelConstant.DATA_DIR_NAME));
            createDir(FileUtils.resolvePath(rootPath, LabelConstant.LABEL_DIR_NAME));
            createDir(FileUtils.resolvePath(rootPath, LabelConstant.AUTO_DIR_NAME));
            createDir(FileUtils.resolvePath(rootPath, LabelConstant.TEST_DIR_NAME));
            createDir(FileUtils.resolvePath(rootPath, LabelConstant.CHECK_DIR_NAME));
        }catch (FileException e){
            throw e;
        }

    }
    
    private void createDir(String path){
        try {
            FileUtils.makeDir(path);
        } catch (FileException e){
            throw e;
        }
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

        String dbName = DatasetUtils.getDataTable(dataBaseId);

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

//        createDBTableSQL.append(StringUtils.format(DBCreateConstant.DROP_TABLE, newTableName)).append("\n");

        // 添加建表
        createDBTableSQL.append(StringUtils.format(DBCreateConstant.CREATE_TABLE, newTableName)).append("(").append("\n");

        createDBTableSQL.append(DBCreateConstant.ID_FIELD).append("\n").append(DBCreateConstant.DATA_PATH_FIELD).append("\n").append(DBCreateConstant.LABEL_PATH_FIELD).append("\n");
        for (int i = 0; i < labelConfList.size(); i++) {
            LabelConf labelConf = labelConfList.get(i);
            String labelBaseName = LabelConstant.DATA_FILED_PREF + "_" + labelConf.getId();
            String filedString = StringUtils.format(DBCreateConstant.LABEL_FIELD,
                    labelBaseName + "_" + LabelConstant.DATA_FILED_VALUE,
                    labelBaseName + "_" + LabelConstant.DATA_FILED_POSITION,
                    labelBaseName + "_" + LabelConstant.DATA_FILED_AUTO,
                    labelBaseName + "_" + LabelConstant.DATA_FILED_TEST);
            if (i < labelConfList.size() - 1){
                filedString = filedString + ",\n";
            }
            createDBTableSQL.append(filedString);
        }

        createDBTableSQL.append(");");
        String createSQL = createDBTableSQL.toString();

        log.info(createSQL);
        dbManager.execute(StringUtils.format(DBCreateConstant.DROP_TABLE, newTableName));
        dbManager.execute(createSQL);

    }


    private Boolean isAbsoluteDir(String path){
        return Boolean.TRUE;
    }

    @Override
    public void makeLabelFile(File file) {

    }


}
