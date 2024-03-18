package com.dlabeling.labeling.service;

import com.dlabeling.labeling.domain.vo.SplitVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/14
 */
public interface FileService {
    void uploadData(List<MultipartFile> fileList, Integer datasetID);

    void exportLabel(SplitVO splitVO, HttpServletResponse httpServletResponse);
}
