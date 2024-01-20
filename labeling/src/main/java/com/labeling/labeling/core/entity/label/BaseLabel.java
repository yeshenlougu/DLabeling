package com.labeling.labeling.core.entity.label;

import com.labeling.labeling.core.entity.labelarea.BaseLabelArea;
import lombok.Data;

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
