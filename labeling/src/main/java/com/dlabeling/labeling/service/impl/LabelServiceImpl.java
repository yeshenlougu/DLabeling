package com.dlabeling.labeling.service.impl;

import com.dlabeling.labeling.domain.vo.item.LabelHistoryItem;
import com.dlabeling.labeling.mapper.InterfaceHistoryMapper;
import com.dlabeling.labeling.mapper.LabelHistoryMapper;
import com.dlabeling.labeling.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/24
 */
@Service
public class LabelServiceImpl implements LabelService {

    @Autowired
    private LabelHistoryMapper labelHistoryMapper;

    @Autowired
    private InterfaceHistoryMapper interfaceHistoryMapper;

    @Override
    public List<LabelHistoryItem> getAllLabelHistroyItem() {
        List<LabelHistoryItem> labelHistoryItemList = interfaceHistoryMapper.getAllLabelHistoryItem();
        return labelHistoryItemList;
    }
}
