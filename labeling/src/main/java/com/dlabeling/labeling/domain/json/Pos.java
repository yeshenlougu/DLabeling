package com.dlabeling.labeling.domain.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/4/7
 */
@Data
public class Pos{
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private String name;
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private Position position;
    @JsonInclude
    private String value;
}