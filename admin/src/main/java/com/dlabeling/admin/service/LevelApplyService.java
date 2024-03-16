package com.dlabeling.admin.service;

import com.dlabeling.system.domain.vo.LevelApplyVO;

import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/15
 */
public interface LevelApplyService {

    List<LevelApplyVO> getAllLevelApply(String type);
}
