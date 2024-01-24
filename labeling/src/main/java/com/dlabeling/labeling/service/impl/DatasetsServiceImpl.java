package com.dlabeling.labeling.service.impl;

import com.dlabeling.labeling.mapper.DatasetsMapper;
import com.dlabeling.labeling.service.DatasetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
@Service
public class DatasetsServiceImpl implements DatasetsService {

    @Autowired
    private DatasetsMapper datasetsMapper;

}
