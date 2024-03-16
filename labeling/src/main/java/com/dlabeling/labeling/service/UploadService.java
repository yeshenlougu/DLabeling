package com.dlabeling.labeling.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/14
 */
public interface UploadService {
    void uploadData(List<MultipartFile> fileList, Integer datasetID);
}
