package com.dlabeling.labeling.controller;

import com.dlabeling.common.core.domain.R;
import com.dlabeling.common.exception.BusinessException;
import com.dlabeling.labeling.domain.po.Datas;
import com.dlabeling.labeling.domain.vo.DatasVO;
import com.dlabeling.labeling.domain.vo.DatasetsVO;
import com.dlabeling.labeling.domain.vo.SplitVO;
import com.dlabeling.labeling.service.DatasetsService;
import com.dlabeling.labeling.service.GenerateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public R<List<DatasetsVO>> getDataset(){
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
            List<DatasetsVO> datasetByFilter = datasetsService.getDatasetByFilter(datasetsVO);
            return R.ok(datasetByFilter);
        }catch (BusinessException e){
            return R.fail(e.getMsg());
        }
    }

    @GetMapping("/labelList")
    public R<List<String>> getLabelList(Integer id){
        List<String> labelList = datasetsService.getLabelList(id);
        return R.ok(labelList);
    }

    @GetMapping("/delete")
    public R<String> deleteDataset(Integer id){
        return R.ok();
    }

    @PostMapping("/datas")
    public R<List<DatasVO>> getDataOfDataset(@RequestBody Map<String, Object> data){
        Integer id = (Integer) data.get("id");
        Integer start = (Integer) data.get("start");
        Integer end = (Integer) data.get("end");
        List<DatasVO> datasBySetID = datasetsService.getDatasBySetID(id, start, end);
        return R.ok(datasBySetID);
    }

    @PostMapping("/splitList")
    public R<List<String>> getSplitList(@RequestBody Map<String, Object> data){
        Integer datasetId = (Integer) data.get("id");
        String type = (String) data.get("type");

        List<SplitVO> splitVOListByID = datasetsService.getSplitVOListByID(datasetId, type);
        List<String> res = splitVOListByID.stream().map(SplitVO::getName).collect(Collectors.toList());
        return R.ok(res);
    }

    @PostMapping("/interfaceList")
    public R<String> getInterfaceList(){
        return R.ok();
    }
}
