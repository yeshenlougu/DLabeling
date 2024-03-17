package com.dlabeling.labeling.domain.vo;

import lombok.Data;

import java.util.Map;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/14
 */
@Data
public class DatasFilterVO {

    private int id;

//    private int start;
//
//    private int end;

    private Map<String, String> labelValueMap;

}
