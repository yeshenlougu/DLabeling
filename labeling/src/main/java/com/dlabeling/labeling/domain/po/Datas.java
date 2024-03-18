package com.dlabeling.labeling.domain.po;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/8
 */
@Data
public class Datas {

    private Integer id;

    private String dataPath;

    private String labelPath;

    private Map<String, Object> labelMap;

    public static Datas convertMapToDatas(Map<String, Object> map){
        Datas datas = new Datas();
        datas.setId((Integer) map.get("id"));
        datas.setDataPath((String) map.get("data_path"));
        datas.setLabelPath((String) map.get("label_path"));

        Map<String, Object> labelMap = new HashMap<>();
        for (String key: map.keySet()){
            if (!key.equals("id") && !key.equals("data_path") && !key.equals("label_path")){
                labelMap.put(key, map.get(key));
            }
        }
        datas.setLabelMap(labelMap);
        return datas;
    }
}
