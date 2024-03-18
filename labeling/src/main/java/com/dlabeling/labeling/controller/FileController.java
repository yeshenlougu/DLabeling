package com.dlabeling.labeling.controller;

import com.dlabeling.common.core.domain.R;
import com.dlabeling.common.utils.ServletUtils;
import com.dlabeling.labeling.domain.vo.SplitVO;
import com.dlabeling.labeling.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/14
 */

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;
    @PostMapping("/upload/data")
    public R<String> uploadData(List<MultipartFile> file, Integer datasetID){
        fileService.uploadData(file, datasetID);


        return R.ok();
    }

    @PostMapping("/exportLabel")
    public R<String> exportLabel(@RequestBody SplitVO splitVO){
        HttpServletResponse httpServletResponse = ServletUtils.getHttpServletResponse();
        fileService.exportLabel(splitVO, httpServletResponse);
        return R.ok();
    }
}

