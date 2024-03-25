package com.dlabeling.labeling.controller;

import com.dlabeling.common.core.domain.R;
import com.dlabeling.labeling.domain.po.InterfaceAddress;
import com.dlabeling.labeling.domain.vo.DatasVO;
import com.dlabeling.labeling.domain.vo.DoLabelVO;
import com.dlabeling.labeling.domain.vo.InterfaceHistoryVO;
import com.dlabeling.labeling.domain.vo.InterfaceVO;
import com.dlabeling.labeling.service.InterfaceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/24
 */
@Slf4j
@RestController
@RequestMapping("/interface")
public class InterfaceController {

    @Autowired
    private InterfaceService interfaceService;

    /**
     * 获取接口列表
     * @return
     */
    @PostMapping("/interfaceList")
    public R<List<InterfaceAddress>> getInterfaceList(Integer datasetID, String type){
        List<InterfaceAddress> interfaceAddressList = interfaceService.getInterfaceList(datasetID, type);
        return R.ok(interfaceAddressList);
    }
    /**
     * 获取所有接口信息
     */
    @GetMapping("/getAllInterface")
    public R<List<InterfaceVO>> getAllInterface(){
        List<InterfaceVO> interfaceVOList = interfaceService.getAllInterface();
        return R.ok(interfaceVOList);
    }

    /**
     *
     */
    @GetMapping("/interfaceHistory/list")
    public R<List<InterfaceHistoryVO>> getInterfaceHistoryVOList(@Param("id") Integer id, @Param("type") String type){
        List<InterfaceHistoryVO> interfaceHistoryList = interfaceService.getInterfaceHistoryList(id, type);
        return R.ok(interfaceHistoryList);
    }

    @PostMapping("/interfaceHistory/datas")
    public R<List<DatasVO>> getLabelHistoryDatasList(@RequestBody InterfaceHistoryVO interfaceHistory){
        List<DatasVO> datasVOList = interfaceService.getLabelHistoryDatasList(interfaceHistory);
        return R.ok(datasVOList);
    }

    @PostMapping("/interface/link")
    public R<String> checkOrTestData(@RequestBody DoLabelVO doLabelVO){

        interfaceService.doLabelInterface(doLabelVO);
        return R.ok();
    }

}
