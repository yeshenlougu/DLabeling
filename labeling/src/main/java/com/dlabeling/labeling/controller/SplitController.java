package com.dlabeling.labeling.controller;

import com.dlabeling.common.core.domain.R;
import com.dlabeling.common.exception.BusinessException;
import com.dlabeling.labeling.domain.vo.DatasVO;
import com.dlabeling.labeling.domain.vo.SetItem;
import com.dlabeling.labeling.domain.vo.SplitVO;
import com.dlabeling.labeling.service.SplitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/25
 */
@Slf4j
@RestController
@RequestMapping("/split")
public class SplitController {

    @Autowired
    SplitService splitService;

    /**
     * 查询获取训练集、测试集列表
     * @param data
     * @return
     */
    @PostMapping("/splitList")
    public R<List<SplitVO>> getSplitList(@RequestBody Map<String, Object> data){
        Integer datasetId = (Integer) data.get("id");
        String type = (String) data.get("type");

        List<SplitVO> splitVOListByID = splitService.getSplitVOListByID(datasetId, type);

        return R.ok(splitVOListByID);
    }

    @PostMapping("/splitData")
    public R<List<DatasVO>> getSplitDatas(Integer datasetID, Integer splitID){
        List<DatasVO> datasVOList = splitService.getSplitDatas(datasetID, splitID);
        return R.ok(datasVOList);
    }

    @PostMapping("/addData")
    public R<String> addDataToSplit(Integer datasetID, Integer splitID, @RequestParam("dataIdList") ArrayList<Integer> dataIdList){
        try {
            splitService.addDataToSplit(datasetID, splitID, dataIdList);
            return R.ok();
        }catch (BusinessException e){
            log.error(e.getMsg());
            return R.fail(e.getMsg());
        }
    }

    @PostMapping("/create")
    public R<String> createSplit(@RequestBody SplitVO splitVO){
        splitService.addSplit(splitVO);
        return R.ok();
    }

    @DeleteMapping("/deleteSplit")
    public R<String> deleteSplit(Integer splitID){
        splitService.deleteSplit(splitID);
        return R.ok("成功删除划分集");
    }

    @PostMapping("/updateSplit")
    public R<String> updateSplit(@RequestBody SplitVO splitVO){
        splitService.updateSplit(splitVO);
        return R.ok("划分集更新成功");
    }

    @PostMapping("/getAllSplitByType")
    public R<List<SetItem>> getAllSplitByType(String type){
        List<SetItem> setItemList = splitService.getAllSplitByType(type);
        return R.ok(setItemList);
    }
}
