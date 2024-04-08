package com.dlabeling.labeling.domain.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/4/7
 */
@Data
public class LabelJson {
    private List<String> labelList;
    private Integer id;
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private String dataPath;
    private List<Pos> labels;
}