package com.dlabeling.labeling.core.entity.label;

import com.dlabeling.labeling.core.entity.labelarea.BaseLabelArea;
import lombok.Data;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
@Data
public class BaseLabel {

    /**
     * 标注的序号
     */
    private String id;

    /**
     * 标签名称
     */
    private String tag;

    /**
     * 内容
     */
    private String context;

    /**
     * 标注的区域
     */
    private BaseLabelArea labelArea;
}
