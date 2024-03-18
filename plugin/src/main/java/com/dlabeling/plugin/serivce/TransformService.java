package com.dlabeling.plugin.serivce;

import java.io.File;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/17
 */
public interface TransformService {
    File transform(String jsonImage, String simpleDatasetImage, File baseLabelFile);
}
