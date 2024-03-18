package com.dlabeling.labeling.utils;

import com.alibaba.fastjson2.JSON;
import com.dlabeling.common.utils.FileUtils;
import com.dlabeling.common.utils.StringUtils;
import com.dlabeling.labeling.domain.po.Datas;
import com.dlabeling.labeling.domain.vo.DatasVO;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/18
 */
public class LabelWriteUtils {

    public static File writeLabelJSON(String filePath, DatasVO datasVO, List<String> labelList) throws IOException {
        if (FileUtils.exists(filePath)){
            File file = new File(filePath);
                file.delete();
        }
        File file = new File(filePath);

        Map<String, Object> jsonString = new HashMap<>();
        jsonString.put("id", String.valueOf(datasVO.getId()));
        jsonString.put("dataPath", datasVO.getFilePath());
        jsonString.put("labelList", JSON.toJSONString(labelList));
        List<Map<String, Object>> labels = new ArrayList<>();
        for (String label : labelList) {
            Map<String, Object> labelMap = new HashMap<>();
            String value = datasVO.getLabelList().get(label + "_value");
            if (value == null){
                value = "";
            }
            String pos = datasVO.getLabelList().get(label + "_pos");
//            String rectangle = JSON.parse(pos, String.class);

            if (pos == null){
                pos ="";
            }
            labelMap.put("value", value);
            labelMap.put("name", label);
            labelMap.put("position", pos);

            labels.add(labelMap);
        }
        jsonString.put("labels", labels);
        FileWriter fileWriter =new FileWriter(file);
        String res = JSON.toJSONString(jsonString);
        fileWriter.write(res);
        fileWriter.flush();
        fileWriter.close();
        return file;
    }

    public static void writeJSON(String filePath, String json){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath));
            fileOutputStream.write(json.getBytes());
        }catch (IOException e){

        }
    }

    public static String updateMapToJSON(){
        return null;
    }

    public static Map<String, Object> updateJSONToMap(){
        return null;
    }
}
