package com.dlabeling.admin.service;

import com.dlabeling.common.enums.UserRole;
import com.dlabeling.labeling.domain.po.Datasets;
import com.dlabeling.labeling.mapper.DatasetsMapper;
import com.dlabeling.system.domain.po.LevelApply;
import com.dlabeling.system.domain.po.user.UserInfo;
import com.dlabeling.system.domain.vo.LevelApplyVO;
import com.dlabeling.system.enums.LevelApplyType;
import com.dlabeling.system.mapper.LevelApplyMapper;
import com.dlabeling.system.mapper.user.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/15
 */
@Service
public class LevelApplyServiceImpl implements LevelApplyService{

    @Autowired
    LevelApplyMapper levelApplyMapper;

    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    DatasetsMapper datasetsMapper;

    @Override
    public List<LevelApplyVO> getAllLevelApply(String type) {
        Integer typeNum = LevelApplyType.getLevelApplyTypeByType(type).getCode();
        List<LevelApply> allLevelApply = levelApplyMapper.getAllLevelApply(typeNum);

        List<LevelApplyVO> levelApplyVOList = allLevelApply.stream().map(LevelApplyVO::converToLevelApplyVO).collect(Collectors.toList());

        Set<Integer> userIdList = new HashSet<>();
        for (LevelApply levelApply : allLevelApply) {
            userIdList.add(levelApply.getApplyer());
            userIdList.add(levelApply.getJudger());
        }
        List<UserInfo> userInfoList = userInfoMapper.getUserInfoByListID(userIdList);
        Map<Integer, String> userIdToName = new HashMap<>();

        for (UserInfo userInfo : userInfoList) {
            userIdToName.put(userInfo.getUserId(), userInfo.getUsername());
        }

        for (LevelApplyVO levelApplyVO : levelApplyVOList) {
            levelApplyVO.setApplyerName(userIdToName.get(levelApplyVO.getApplyer()));
            levelApplyVO.setJugerName(userIdToName.get(levelApplyVO.getJudger()));
            if (LevelApplyType.getLevelApplyTypeByType(type) == LevelApplyType.USER_APPLY){
                levelApplyVO.setPrivilegeName(UserRole.getRoleByCode(Integer.parseInt(levelApplyVO.getPrivilege())).getRole());
            }else {
                Datasets datasets = datasetsMapper.selectByID(Integer.valueOf(levelApplyVO.getPrivilege()));
                levelApplyVO.setPrivilegeName(datasets.getName());
            }
        }

        return levelApplyVOList;
    }
}
