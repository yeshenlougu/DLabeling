package com.dlabeling.labeling.service;

import com.dlabeling.labeling.domain.vo.item.LabelHistoryItem;

import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/24
 */
public interface LabelService {
    List<LabelHistoryItem> getAllLabelHistroyItem();
}
