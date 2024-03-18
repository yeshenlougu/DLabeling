package com.dlabeling.plugin.serivce.impl;

import com.dlabeling.plugin.serivce.TransformService;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/17
 */

@Service
public class TransformServiceImpl implements TransformService {
    @Override
    public File transform(String jsonImage, String simpleDatasetImage, File baseLabelFile) {
        return baseLabelFile;
//        return null;
    }
}
