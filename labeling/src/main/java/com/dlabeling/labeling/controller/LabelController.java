package com.dlabeling.labeling.controller;

import com.dlabeling.common.core.domain.R;
import com.dlabeling.labeling.domain.vo.LabelHistoryVO;
import com.dlabeling.labeling.domain.vo.item.LabelHistoryItem;
import com.dlabeling.labeling.service.LabelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/24
 */

@Slf4j
@RestController
@RequestMapping("/label")
public class LabelController {

    @Autowired
    private LabelService labelService;


    @GetMapping("/getAllLabelHistory")
    public R<List<LabelHistoryVO>> getAllLabelHistory(){
        List<LabelHistoryVO> labelHistoryVOList=labelService.getAllLabelHistoryVO();
        return R.ok(labelHistoryVOList);
    }

    @PostMapping("/getLabelHistoryInfo")
    public R<LabelHistoryVO> getLabelHistoryInfo(Integer datasetID, Integer labelHistoryID){
        LabelHistoryVO labelHistoryVO = labelService.getLabelHistoryVO(datasetID, labelHistoryID);
        return R.ok(labelHistoryVO);
    }
}
