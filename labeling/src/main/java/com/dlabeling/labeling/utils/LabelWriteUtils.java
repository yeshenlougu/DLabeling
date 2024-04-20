package com.dlabeling.labeling.utils;

import com.alibaba.fastjson2.JSON;
import com.dlabeling.common.utils.FileUtils;
import com.dlabeling.common.utils.StringUtils;
import com.dlabeling.labeling.domain.json.LabelJson;
import com.dlabeling.labeling.domain.json.Pos;
import com.dlabeling.labeling.domain.json.Position;
import com.dlabeling.labeling.domain.po.Datas;
import com.dlabeling.labeling.domain.vo.DatasVO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;

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
        LabelJson labelJson = new LabelJson();
        labelJson.setId(datasVO.getId());
        labelJson.setDataPath(datasVO.getFilePath());
        labelJson.setLabelList(labelList);
        labelJson.setLabels(new ArrayList<>());
        for (String label : labelList) {
            Pos pos = new Pos();
            pos.setName(label);
            pos.setValue(datasVO.getLabelList().get(label+"_value"));
            Position position = JSON.parseObject(datasVO.getLabelList().get(label + "_pos"), Position.class);
            pos.setPosition(position);
            labelJson.getLabels().add(pos);
        }
        String res = JSON.toJSONString(labelJson, String.valueOf(SerializationFeature.WRITE_NULL_MAP_VALUES));

        FileWriter fileWriter =new FileWriter(file);
        fileWriter.write(res);
        fileWriter.flush();
        fileWriter.close();
        return file;
    }

    public static void writeJSON(String filePath, String json){
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File(filePath));
            fileOutputStream.write(json.getBytes());
            fileOutputStream.close();
        }catch (IOException e){
        
        }finally {
        }
    }

    public static String updateMapToJSON(){
        return null;
    }

    public static Map<String, Object> updateJSONToMap(){
        return null;
    }
}
