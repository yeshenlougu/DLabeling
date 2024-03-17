package com.dlabeling.labeling.domain.vo;

import com.dlabeling.common.utils.FileUtils;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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
    private String filePath;
    private String labelPath;
    private Map<String, String> labelList;


    private String file;
    private Integer datasetID;

    public static DatasVO convertMapToDatasVO(Map<String, Object> datasMap, Map<String, String> fieldToLabel) throws IOException {

        String dataPath = (String) datasMap.get("data_path");
        String base64File = FileUtils.getBase64(dataPath);
        DatasVO datasVO = new DatasVO();
        datasVO.setId((Integer) datasMap.get("id"));
        datasVO.setFileName(FileUtils.removeFileExtension(FileUtils.getFileName(dataPath)));
        datasVO.setFilePath(dataPath);
        datasVO.setFile(base64File);
        datasVO.setLabelPath((String) datasMap.get("label_path"));
        Map<String, String> labelList = new HashMap<>();
        datasMap.remove("id");
        datasMap.remove("data_path");
        datasMap.remove("label_path");
        for (String key : datasMap.keySet()) {
            String newKey = key;
            int i = key.lastIndexOf("_");
            String suffix = key.substring(i+1);
            String pref = key.substring(0, i);
            newKey = fieldToLabel.get(pref) + "_" + suffix;
            labelList.put(newKey, (String)datasMap.get(key));
        }
        datasVO.setLabelList(labelList);
        return datasVO;

    }
}
