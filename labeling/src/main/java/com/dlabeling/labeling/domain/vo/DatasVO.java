package com.dlabeling.labeling.domain.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/8
 */

@Data
public class DatasVO {
    private Integer id;
    private String fileName;
    private Map<String, Object> labelList;

    private String file;
}
