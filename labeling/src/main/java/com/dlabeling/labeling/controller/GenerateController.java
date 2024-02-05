package com.dlabeling.labeling.controller;

import com.dlabeling.labeling.domain.vo.DatasetsVO;
import com.dlabeling.labeling.service.GenerateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/28
 */

@Slf4j
@RestController
@RequestMapping("/generate/")
public class GenerateController {

    @Autowired
    GenerateService generateService;

    @PostMapping("/createDB")
    public void createDB(DatasetsVO datasetsVO){
        log.info("in");
        generateService.makeDataBase(datasetsVO);

    }
}
