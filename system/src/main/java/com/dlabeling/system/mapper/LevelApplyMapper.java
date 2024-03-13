package com.dlabeling.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dlabeling.system.domain.po.LevelApply;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/2
 */
@Mapper
public interface LevelApplyMapper extends BaseMapper<LevelApply> {

    int addLevelApply(LevelApply levelApply);

    int updateLevelApply(LevelApply levelApply);

    List<LevelApply> getAllLevelApply(Integer type);

    List<LevelApply> getLevelApplyByStatus(int status);
}
