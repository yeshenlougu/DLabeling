package com.dlabeling.labeling.controller;

import com.dlabeling.common.core.domain.R;
import com.dlabeling.common.exception.BusinessException;
import com.dlabeling.labeling.domain.vo.*;
import com.dlabeling.labeling.domain.vo.SetItem;
import com.dlabeling.labeling.service.DatasetsService;
import com.dlabeling.labeling.service.GenerateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/28
 */

@Slf4j
@RestController
@RequestMapping("/dataset/")
public class DatasetController {

    @Autowired
    GenerateService generateService;

    @Autowired
    DatasetsService datasetsService;

    @PostMapping("/createDB")
    public R<String> createDB(@RequestBody DatasetsVO datasetsVO){
        log.info("in");
        try {
            generateService.makeDataBase(datasetsVO);
            return R.ok("数据集创建成功");
        }catch (BusinessException e){
            return R.fail(e.getMsg());
        }

    }

    @GetMapping("/getDataset")
    public R<DatasetsVO> getDataset(Integer id){
        try {
            DatasetsVO datasetByID = datasetsService.getDatasetByID(id);
            return R.ok(datasetByID);
        }catch (BusinessException e){
            return R.fail(e.getMsg());
        }
    }


    @GetMapping("/getAllDataset")
    public R<List<DatasetsVO>> getAllDataset(){
        try {
            List<DatasetsVO> allDatasets = datasetsService.getAllDatasets();
            return R.ok(allDatasets);
        }catch (BusinessException e){
            return R.fail(e.getMsg());
        }
    }

    @PostMapping("/getDatasetByFilter")
    public R<List<DatasetsVO>> getDatasetByFilter(@RequestBody DatasetsVO datasetsVO){
        try {
            List<DatasetsVO> allDatasets = datasetsService.getDatasetByFilter(datasetsVO);
            return R.ok(allDatasets);
        }catch (BusinessException e){
            return R.fail(e.getMsg());
        }
    }

    @GetMapping("/datasetHas")
    public R<List<DatasetsVO>> getDatasetHas(){
        try {
            List<DatasetsVO> allDatasets = datasetsService.getDatasetHas();
            return R.ok(allDatasets);
        }catch (BusinessException e){
            return R.fail(e.getMsg());
        }
    }

    @GetMapping("/datasetOther")
    public R<List<DatasetsVO>> getDatasetDontHas(){
        try {
            List<DatasetsVO> allDatasets = datasetsService.getDatasetDontHas();
            return R.ok(allDatasets);
        }catch (BusinessException e){
            return R.fail(e.getMsg());
        }
    }
    /**
     * 创建者列表
     * @return
     */
    @GetMapping("/creatorList")
    public R<List<String>> getCreatorList(){
        List<String> creatorList = datasetsService.getCreatorList();
        return R.ok(creatorList);
    }

    /**
     * 数据集标签列表
     * @param id
     * @return
     */
    @GetMapping("/labelList")
    public R<List<String>> getLabelList(Integer id){
        List<String> labelList = datasetsService.getLabelList(id);
        return R.ok(labelList);
    }

    /**
     * 删除数据集
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public R<String> deleteDataset(Integer id){
        return R.ok();
    }

    /**
     * 分页 获取数据
     * @param data
     * @return
     */
    @PostMapping("/datas")
    public R<List<DatasVO>> getDataOfDataset(@RequestBody Map<String, Object> data){
        Integer id = (Integer) data.get("id");
        Integer start = (Integer) data.get("start");
        Integer end = (Integer) data.get("end");
        List<DatasVO> datasBySetID = datasetsService.getDatasBySetID(id);
        return R.ok(datasBySetID);
    }

    @PostMapping("/getDatasByID")
    public R<DatasVO> getDataByID(Integer datasetID, Integer dataID){
        DatasVO datasVO = datasetsService.getDatasByID(datasetID, dataID);
        return R.ok(datasVO);
    }

    @PostMapping("/datas/filter")
    public R<List<DatasVO>> getDatasByFilter(@RequestBody DatasFilterVO datasFilterVO){
        List<DatasVO> datasVOList = datasetsService.getDatasByFilter(datasFilterVO);

        return R.ok(datasVOList);
    }

    @PostMapping("/data/batchEdit")
    public R<String> batchEditDatas(@RequestBody DatasEditVO datasEditVO){
        try {
            Map<String, Integer> editForm = datasEditVO.getEditForm();
            datasetsService.batchEditDatas(datasEditVO);
            return R.ok();
        }catch (BusinessException e){
            return R.fail(e.getMessage());
        }

    }

    @PostMapping("/datas/update")
    public R<String> updateDatas(@RequestBody DatasVO datasVO){
        datasetsService.updateDatas(datasVO);
        return R.ok();
    }

    @PostMapping("/dataset/update")
    public R<String> updateDatasetInfo(@RequestBody DatasetsVO datasetsVO) {
        datasetsService.updateDatasetInfo(datasetsVO);
        return R.ok();
    }


}
